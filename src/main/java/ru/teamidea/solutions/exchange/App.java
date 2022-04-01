package ru.teamidea.solutions.exchange;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class.getName());
    private static final String LINK = "http://www.cbr.ru/scripts/XML_daily.asp";

    private double getPrice(Document document, String id) {
        Element element = document.getElementById(id);
        int nominal = Integer.parseInt(element.child(2).text());
        double value = Double.parseDouble(element.child(4).text().replace(',', '.'));
        return value / nominal;
    }

    public void run() {
        Connection connection = Jsoup.connect(LINK);
        try {
            Document document = connection.get();
            double forint = getPrice(document, "R01135");
            double krone = getPrice(document, "R01535");
            System.out.printf("One Norwegian krone costs %.2f Hungarian forints.%n", krone / forint);
        } catch (IOException e) {
            LOG.error("Impossible to get Document from Jsoup Connection.", e);
        }
    }

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }
}
