package fun.lzwi.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import javafx.concurrent.Worker;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import fun.lzwi.bean.Book;
import fun.lzwi.epubime.Epub;
import fun.lzwi.epubime.EpubFile;
import fun.lzwi.epubime.EpubReader;
import fun.lzwi.epubime.Resource;
import fun.lzwi.epubime.StringResourceReader;
import fun.lzwi.epubime.document.NCX;
import fun.lzwi.manager.SceneManager;
import fun.lzwi.util.PageUtils;
import fun.lzwi.util.ResUtils;
import fun.lzwi.util.WebUtils;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class ViewController {

    @FXML
    private HBox body;

    @FXML
    private VBox vbox;
    @FXML
    private ListView<String> listView;
    @FXML
    private WebView webView;
    // private HTMLEditor htmlEditor;

    private Book book;

    private Resource res;

    public ViewController(Book book) {
        this.book = book;
    }

    @FXML
    private void initialize() throws IOException, ParserConfigurationException, SAXException {
        webView.prefWidthProperty().bind(body.widthProperty());
        vbox.prefHeightProperty().bind(body.heightProperty());
        vbox.maxWidthProperty().bind(listView.widthProperty());
        SceneManager.getInstance().setTitle(book.getTitle());

        EpubReader epubReader = new EpubReader(new EpubFile(new File(book.getFile())));
        Epub epub = epubReader.read();

        NCX ncx = epub.getNCX();
        Map<String, String> map = new HashMap<>();
        List<String> contents = ncx.getNavMap().stream().map(p -> {
            map.put(p.getNavLabel(), p.getContent());
            return p.getNavLabel();
        }).collect(Collectors.toList());

        webView.getEngine().onErrorProperty().addListener((ov, o, n) -> {
            System.out.println(n);
        });
        webView.getEngine().loadContent(ResUtils.readHtml("book.html"));
        loadIndex(contents);

        Resource resource = new Resource(new EpubFile(new File(book.getFile())));
        resource.setHref(ncx.getHref());
        listView.getSelectionModel().selectedItemProperty().addListener((ov, o, n) -> {
            String content = map.get(listView.getSelectionModel().getSelectedItem());
            try {
                StringResourceReader reader = new StringResourceReader();
                Resource html = new Resource(resource, content);
                String text = reader.read(html);
                // webView.getEngine().loadContent(text);
                // JSObject window = (JSObject)
                webView.getEngine().executeScript(String.format("book.load('%s');",
                PageUtils.getBody(text)));
                System.out.println("加载html - " + html.getHref());
                res = html;
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        listView.getSelectionModel().selectFirst();
        webView.getEngine().documentProperty()
                .addListener((ChangeListener<Document>) (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        // PageUtils.processCSS(newValue, res);
                        // PageUtils.processImg(newValue, res);
                        // PageUtils.inject(newValue);
                        // webView.getEngine().reload();

                    }
                });

        webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webView.getEngine().executeScript("window");
                window.setMember("kuengi", new WebUtils());
                System.out.println("inject");
                // webView.getEngine().executeScript("inject();");
                // webView.getEngine()
                //         .executeScript("window.onload();");
                // window.getMember("book");
                // webView.getEngine()
                //         .executeScript(String.format("book.load('%s');", "text"));

            }
        });
    }

    private void loadIndex(List<String> contents) {
        listView.getItems().addAll(contents);
    }

    public void quit() {
        SceneManager.getInstance().back();
    }
}
