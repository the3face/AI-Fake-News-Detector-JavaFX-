package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import java.util.Arrays;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();

        // Use the interface type
        RSSScraperService scraper = new RSSScraper();

        var feeds = Arrays.asList(
                "https://news.google.com/rss",
                "https://rss.cnn.com/rss/edition.rss",
                "https://feeds.bbci.co.uk/news/rss.xml",
                "https://www.aljazeera.com/xml/rss/all.xml",
                "https://www.reutersagency.com/feed/?best-topics=world",
                "https://www.theguardian.com/world/rss"
        );

        try {
            scraper.scrapeFeeds(feeds, "multi_rss_output.txt");
            System.out.println("Scraping complete!");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

}