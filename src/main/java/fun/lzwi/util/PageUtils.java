package fun.lzwi.util;

import java.io.IOException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import fun.lzwi.epubime.BytesResourceReader;
import fun.lzwi.epubime.Resource;
import fun.lzwi.epubime.StringResourceReader;
import fun.lzwi.epubime.util.XmlUtils;

public class PageUtils {

    // <meta name="viewport" content="width=device-width, initial-scale=1" />
    public static void processViewport(Document document) {
        Element meta = document.createElement("meta");
        meta.setAttribute("name", "viewport");
        meta.setAttribute("content", "width=device-width, initial-scale=1");

        Node head = document.getElementsByTagName("head").item(0);
        if (head == null) {
            head = document.createElement("head");
            document.getDocumentElement().appendChild(head);
        }
        head.appendChild(meta);
    }

    public static void processCSS(Document document, Resource resource) {
        NodeList links = document.getElementsByTagName("link");
        XmlUtils.foreachNodeList(links, node -> {
            if ("stylesheet".equals(XmlUtils.getNodeAttribute(node, "rel"))
                    && "text/css".equals(XmlUtils.getNodeAttribute(node, "type"))) {
                String href = XmlUtils.getNodeAttribute(node, "href");
                Resource styles = new Resource(resource, href);
                try {
                    String css = new StringResourceReader().read(styles);
                    Element styleElement = node.getOwnerDocument().createElement("style");
                    styleElement.setTextContent(css);
                    node.getParentNode().appendChild(styleElement);
                    System.out.println("读取css - " + href);
                } catch (IOException e) {
                    System.err.println("读取" + href + "异常");
                }
            }

        });
    }

    public static void processImg(Document document, Resource resource) {
        NodeList links = document.getElementsByTagName("img");
        XmlUtils.foreachNodeList(links, node -> {
            String src = XmlUtils.getNodeAttribute(node, "src");
            if (src == null || "".equals(src) || src.startsWith("data:")) {
                return;
            }
            Resource res = new Resource(resource, src);
            try {
                String base64 = Base64.getEncoder().encodeToString(new BytesResourceReader().read(res));
                String img = "data:image/jpeg;base64," + base64;
                node.getAttributes().getNamedItem("src")
                        .setTextContent(img);
                System.err.println("读取图片 - " + res.getHref());
            } catch (Exception e) {
                System.err.println("读取" + res.getHref() + "异常");
            }
        });
    }

    public static void inject(Document document) {
        Node head = document.getElementsByTagName("head").item(0);
        if (head == null) {
            head = document.createElement("head");
            document.getDocumentElement().appendChild(head);
        }
        // <style>
        Element style = document.createElement("link");
        // style.setTextContent(ResUtils.readString("css/book-style.css"));
        style.setAttribute("href", ResUtils.getResourceURL("css/pure.min.css"));
        style.setAttribute("rel", "stylesheet");
        head.appendChild(style);

        // <link href="https://cdn.bootcdn.net/ajax/libs/pure/3.0.0/pure.min.css"
        // rel="stylesheet">
        Element pure = document.createElement("link");
        pure.setAttribute("href", ResUtils.getResourceURL("css/pure.min.css"));
        pure.setAttribute("rel", "stylesheet");
        head.appendChild(pure);
        // <link href="https://cdn.bootcdn.net/ajax/libs/heti/0.9.4/heti.min.css"
        // rel="stylesheet">
        Element heti = document.createElement("link");
        heti.setAttribute("href", ResUtils.getResourceURL("css/heti.min.css"));
        heti.setAttribute("rel", "stylesheet");
        head.appendChild(heti);
        // <script defer
        // src="https://cdn.bootcdn.net/ajax/libs/alpinejs/3.13.0/cdn.min.js"></script>
        Element alpine = document.createElement("script");
        alpine.setAttribute("src", ResUtils.getResourceURL("js/alpine.min.js"));
        alpine.setAttribute("defer", null);
        head.appendChild(alpine);
        // <script
        // src="https://cdn.bootcdn.net/ajax/libs/zepto/1.2.0/zepto.min.js"></script>
        Element zepto = document.createElement("script");
        zepto.setAttribute("src", ResUtils.getResourceURL("js/zepto.min.js"));
        head.appendChild(zepto);
        // <script>
        // Element script = ((HTMLDocument) document).createElement("script");
        // String sc = ResUtils.readString("js/book-script.js");
        // ((HTMLScriptElement)
        // script).setSrc(ResUtils.getResourceURL("js/book-script.js"));
        // head.appendChild(script);

        System.out.println("injected");
    }

    public static String getBody(String html) {
        // String html = "<html><head><title>Test</title></head><body><p>This is a test.</p></body></html>";

        String bodyContent = "";

        Pattern pattern = Pattern.compile("<body>(.*?)</body>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(html);

        if (matcher.find()) {
            bodyContent = matcher.group(1);
        }

        System.out.println(bodyContent); // Prints: <p>This is a test.</p>
        return bodyContent;
    }

}
