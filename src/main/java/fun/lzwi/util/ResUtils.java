package fun.lzwi.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
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
        BufferedReader br = new BufferedReader(new InputStreamReader(resource));
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
}
