package fun.lzwi;

import java.io.IOException;

import fun.lzwi.javafx.element.ELResources;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getClassLoader().getResource("main.fxml"));
        var scene = new Scene(fxmlLoader.load(), 640, 480);
        scene.getStylesheets().add(ELResources.loadStyle().toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

}