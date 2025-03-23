package fun.lzwi.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import fun.lzwi.epubime.epub.EpubBook;
import fun.lzwi.epubime.epub.EpubParseException;
import fun.lzwi.epubime.epub.EpubParser;
import javafx.concurrent.Worker;
import javafx.scene.control.Button;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import fun.lzwi.bean.Book;
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
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;

    @FXML
    private ListView<String> listView;
    @FXML
    private WebView webView;
    // private HTMLEditor htmlEditor;

    private Book book;


    public ViewController(Book book) {
        this.book = book;
    }

    @FXML
    private void initialize() throws IOException, ParserConfigurationException, SAXException, EpubParseException {
        webView.prefWidthProperty().bind(body.widthProperty());
        vbox.prefHeightProperty().bind(body.heightProperty());
        vbox.maxWidthProperty().bind(listView.widthProperty());
        SceneManager.getInstance().setTitle(book.getTitle());
        EpubParser parser = new EpubParser(new File(book.getFile()));
        EpubBook epubBook = parser.parse();
        Map<String, String> map = new HashMap<>();
        List<String> contents = epubBook.getChapters().stream().map(p -> {
            map.put(p.getTitle(), p.getContent());
            return p.getTitle();
        }).collect(Collectors.toList());

        webView.getEngine().onErrorProperty().addListener((ov, o, n) -> {
            System.out.println(n);
        });
        webView.getEngine().loadContent(ResUtils.readHtml("book.html"));
        loadIndex(contents);

        listView.getSelectionModel().selectedItemProperty().addListener((ov, o, n) -> {
            String content = map.get(listView.getSelectionModel().getSelectedItem());
            epubBook.getResources().stream().filter((r) -> r.getHref().endsWith(content)).findFirst().ifPresent((r) -> {
                System.out.println(r.getHref());
                String body = PageUtils.getBody(new String(r.getData(), StandardCharsets.UTF_8));
                // base64
                body = Base64.getEncoder().encodeToString(body.getBytes());
                String js = String.format("book.load('%s');", body);
                webView.getEngine().executeScript(js);
                System.out.println("加载html - " + content);
            });
        });
        listView.getSelectionModel().selectFirst();
        webView.getEngine().documentProperty().addListener((ChangeListener<Document>) (observable, oldValue,
                                                                                       newValue) -> {
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

        button1.setOnAction(event -> {
            quit();
        });

        button3.setOnAction(event -> {
            vbox.prefWidthProperty().unbind();
            vbox.setPrefWidth(0);
        });
    }

    private void loadIndex(List<String> contents) {
        listView.getItems().addAll(contents);
    }

    public void quit() {
        SceneManager.getInstance().back();
    }
}
