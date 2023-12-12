package fun.lzwi;

import java.io.IOException;

import fun.lzwi.controller.MainController;
import fun.lzwi.javafx.element.ELResources;
import fun.lzwi.manager.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getClassLoader().getResource("main.fxml"));
        fxmlLoader.setController(new MainController());
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        scene.getStylesheets().add(ELResources.loadStyle().toExternalForm());
        // scene.getStylesheets().add(App.class.getClassLoader().getResource("css/style.css").toExternalForm());
        SceneManager.getInstance().setStage(stage);
        SceneManager.getInstance().push("main", scene);
        stage.setTitle("Kuengi - 一个 Epub 阅读器");
        stage.show();
    }

}