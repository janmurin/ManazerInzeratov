package home.managerinzeratov;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupCrawler {

    static final String LOGIN_URL = "http://www.1000blockov.sk/registrator";
    //static final String REG_URL = "https://narodnablockovaloteria.tipos.sk/sk/administracia/registracia-dokladu";

    private Connection connection;
    private Map<String, String> cookies;
    private String viewState;
    private String eventValidation;
    String connectionUrl;

    public JsoupCrawler() {
    }

    public void connect() {
        String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30";
//        Jsoup.connect("http://example.com").userAgent(ua).get().html();
        connection = Jsoup.connect(getConnectionUrl()).userAgent(ua);
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public void execute() {

    }

    public static void main(String[] args) {
        JsoupCrawler a = new JsoupCrawler();
        a.execute();

    }

    public Document getPage(String string) {
        Document document = null;
        while (document == null) {
            try {
                connectionUrl = string;
                connect();

                connection.method(Method.GET);

                Response response = connection.execute();
                document = response.parse();
                //System.out.println(document);

            } catch (Exception e) {
//                System.out.println("getPage exception: " + e);
//                System.out.println("retrying...");
            }
        }
        //Document document = Jsoup.parse(html);
//        document.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
//        document.select("br").append("\n");
//        document.select("p").prepend("\n\n");
        return document;
    }

    void odosliPripomienku(String text, String hostname) {
        System.out.println("jcrawler: odosielam pripomienku");
        try {
            Connection.Response loginForm = Jsoup.connect("http://www.jmurin.sk/manazer/pripomienka2.php")
                    .method(Connection.Method.GET)
                    .execute();

            Document document = Jsoup.connect("http://www.jmurin.sk/manazer/pripomienka2.php")
                    .data("name", hostname)
                    .data("comment", text)
                    .data("submit", "Submit")
                    .cookies(loginForm.cookies())
                    .post();
            System.out.println(document);
        } catch (IOException e) {
            System.out.println("getPage exception: " + e);
        }
    }

}
