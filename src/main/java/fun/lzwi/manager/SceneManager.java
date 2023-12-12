package fun.lzwi.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    // 单例模式
    // 私有化构造函数
    private SceneManager() {
    }

    private static SceneManager instance = new SceneManager();

    public static SceneManager getInstance() {
        return instance;
    }

    private Stage stage;
    private final Map<String, Scene> scenes = new HashMap<>();
    private final List<String> history = new ArrayList<>();

    /**
     * @return the stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void addScene(String route, Scene scene) {
        scenes.put(route, scene);
    }

    public Scene getScene(String route) {
        return scenes.get(route);
    }

    public List<String> getHistory() {
        return history;
    }

    public void clearHistory() {
        history.clear();
    }

    public void clear() {
        scenes.clear();
        history.clear();
    }

    public boolean isEmpty() {
        return scenes.isEmpty();
    }

    public boolean push(String route, Scene scene) {
        addScene(route, scene);
        return push(route);
    }

    public boolean push(String route) {
        Scene scene = scenes.get(route);
        if (scene != null) {
            stage.setScene(scene);
            history.add(route);
            return true;
        }
        return false;
    }

    public void setTitle(String title) {
        if (stage != null) {
            stage.setTitle(title);
        }
    }

    public boolean back() {
        if (history.size() > 1) {
            history.remove(history.size() - 1);
            String route = history.get(history.size() - 1);
            Scene scene = scenes.get(route);
            if (scene != null) {
                stage.setScene(scene);
                history.remove(route);
                scenes.remove(route);
                return true;
            }
        }
        return false;
    }

    public boolean forward() {
        return false;
    }
}
