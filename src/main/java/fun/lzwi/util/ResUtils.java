package fun.lzwi.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

import fun.lzwi.App;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class ResUtils {
    // get resources
    public static InputStream getResource(String path) {
        return App.class.getClassLoader().getResourceAsStream(path);
    }

    public static String getResourceURL(String path) {
        try {
            return ResUtils.class.getClassLoader().getResource(path).toURI().toURL().toString();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    // read string from path
    public static String readString(String path) {
        InputStream resource = getResource(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            resource.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void writeString(Node doc) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String xmlString = writer.toString();
            System.out.println(xmlString);
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String readHtml(String name) {
        String html = readString(name);
        html = html.replace("./css/pure.min.css", ResUtils.getResourceURL("css/pure.min.css"));
        html = html.replace("./css/heti.min.css", ResUtils.getResourceURL("css/heti.min.css"));
        html = html.replace("./css/book-style.css", ResUtils.getResourceURL("css/book-style.css"));
        html = html.replace("./js/alpine.min.js", ResUtils.getResourceURL("js/alpine.min.js"));
        html = html.replace("./js/zepto.min.js", ResUtils.getResourceURL("js/zepto.min.js"));
        html = html.replace("./js/eruda.min.js", ResUtils.getResourceURL("js/eruda.min.js"));
        html = html.replace("./js/book-script.js", ResUtils.getResourceURL("js/book-script.js"));
        return html;
    }
}
