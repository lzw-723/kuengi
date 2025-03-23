package fun.lzwi.controller;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;

import javax.xml.parsers.ParserConfigurationException;

import fun.lzwi.epubime.epub.EpubBook;
import fun.lzwi.epubime.epub.EpubParseException;
import fun.lzwi.epubime.epub.EpubParser;
import fun.lzwi.epubime.epub.Metadata;
import org.xml.sax.SAXException;

import fun.lzwi.App;
import fun.lzwi.SystemInfo;
import fun.lzwi.bean.Book;
import fun.lzwi.service.BookshelfService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainController {

    // @FXML
    // private Label title;
    // @FXML
    // private Label author;

    @FXML
    private VBox root;

    @FXML
    private StackPane body;

    @FXML
    private Hyperlink link;

    @FXML
    private ListView<String> contents;

    @FXML
    private GridPane bookshelf;

    public void initialize() {
        initLayout();
    }

    public void showSystemInfo() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("系统信息");
        alert.setHeaderText("当前系统信息");
        alert.setContentText(SystemInfo.getInfo());
        alert.show();
    }

    public void initLayout() {
        body.prefHeightProperty().bind(root.heightProperty());
        body.prefWidthProperty().bind(root.widthProperty());
        bookshelf.prefHeightProperty().bind(body.heightProperty().add(-60));
        bookshelf.prefWidthProperty().bind(body.widthProperty());
    }

    public void loadBookshelf() {
        ObservableList<Book> books = BookshelfService.getInstance().observableList();
        // int columns = (int) (bookshelf.getWidth() / 190);
        // int rows = (int) (bookshelf.getHeight() / 190);
        // bookshelf.getColumnConstraints().clear();
        // bookshelf.setGridLinesVisible(true);
        link.setVisible(books.size() == 0);
        for (int i = 0; i < books.size(); i++) {
            if (i + 1 > 3 * 3) {
                return;
            }
            Book book = books.get(i);
            bookshelf.add(getItem(book), i % 3, i / 3);
        }
    }

    public Parent getItem(Book book) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getClassLoader().getResource("item_book.fxml"));
        fxmlLoader.setController(new BookItemController(book));
        Parent item;
        try {
            item = fxmlLoader.load();
            return item;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void openFile() throws ZipException, IOException, ParserConfigurationException, SAXException, EpubParseException {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new ExtensionFilter("epub文件", "*.epub"));
        File file = chooser.showOpenDialog(contents.getScene().getWindow());
        if (file != null) {
            read(file);
        }
    }

    public void openDir() throws IOException, ParserConfigurationException, SAXException, EpubParseException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("选择epub文件夹");
        File file = chooser.showDialog(contents.getScene().getWindow());
        if (file != null) {
            File[] listFiles = file.listFiles();
            for (File file2 : listFiles) {
                if (file2.getName().toLowerCase().endsWith(".epub")) {
                    read(file2);
                }
            }
        }
    }

    public void read(File file) throws IOException, ParserConfigurationException, SAXException, EpubParseException {
        EpubParser parser = new EpubParser(file);
        EpubBook epub = parser.parse();
        Metadata metaData = epub.getMetadata();
        Book book = new Book();
        book.setFile(file.getPath());
        book.setImage(epub.getCover().getData());
        book.setTitle(metaData.getTitle());
        book.setAuthor(metaData.getContributor());
        // book.setImage(metaData.getCoverages().get(0));
        BookshelfService.getInstance().add(book);
        loadBookshelf();
    }

    public void exit() {
        System.exit(0);
    }
}
