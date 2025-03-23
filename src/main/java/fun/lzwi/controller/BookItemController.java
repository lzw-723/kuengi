package fun.lzwi.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

import fun.lzwi.App;
import fun.lzwi.bean.Book;
import fun.lzwi.javafx.element.ELResources;
import fun.lzwi.manager.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BookItemController {
    @FXML
    private Label title;
    @FXML
    private Label author;

    @FXML
    private ImageView img;

    private Book book;

    public BookItemController(Book book) {
        super();
        this.book = book;
    }

    public void initialize() throws IOException {
        initLayout();
    }

    private void initLayout() throws IOException {
        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        if (book.getImage() != null) {
            img.setImage(new Image(new ByteArrayInputStream(book.getImage())));
            return;
        }
        URL noImage = App.class.getClassLoader().getResource("img/image_no.png");
        img.setImage(new Image(noImage.openStream()));
    }

    public void openBook() {
        System.out.println(book.toString());
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getClassLoader().getResource("view.fxml"));
        try {
            fxmlLoader.setController(new ViewController(book));
            Scene scene = new Scene(fxmlLoader.load(), 640, 480);
            scene.getStylesheets().add(ELResources.loadStyle().toExternalForm());
            // scene.getStylesheets().add(App.class.getClassLoader().getResource("css/style.css").toExternalForm());
            SceneManager.getInstance().push("view", scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
