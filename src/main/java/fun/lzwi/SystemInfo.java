package fun.lzwi;

public class SystemInfo {

    public static String javaVersion() {
        return System.getProperty("java.version");
    }

    public static String javafxVersion() {
        return System.getProperty("javafx.version");
    }

    public static String getInfo() {
        return String.format("Java version: %s\nJavaFX version: %s", javaVersion(), javafxVersion());
    }

}