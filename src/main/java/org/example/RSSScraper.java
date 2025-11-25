package org.example;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.util.List;

public class RSSScraper implements RSSScraperService {

    private final HttpClient httpClient;

    public RSSScraper() {
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public void scrapeFeeds(List<String> feedUrls, String outputFilePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            writer.write("RSS Scrape Results - " + ZonedDateTime.now() + "\n");
            writer.write("=================================================\n\n");

            for (String feedUrl : feedUrls) {
                writer.write("Fetching feed: " + feedUrl + "\n");
                writer.write("-----------------------------------------------\n");

                try {
                    String xml = fetch(feedUrl);
                    parseAndWrite(xml, writer);
                } catch (Exception e) {
                    writer.write("Failed to retrieve feed: " + e.getMessage() + "\n\n");
                }

                writer.write("\n\n");
            }
        }
    }

    private String fetch(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    private void parseAndWrite(String xml, BufferedWriter writer) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new java.io.ByteArrayInputStream(xml.getBytes()));

            Element channel = (Element) doc.getElementsByTagName("channel").item(0);
            if (channel == null) {
                writer.write("Not a valid RSS feed.\n\n");
                return;
            }

            String title = getTagText(channel, "title");
            writer.write("Feed Title: " + title + "\n\n");

            NodeList items = channel.getElementsByTagName("item");

            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);

                writer.write("Title: " + getTagText(item, "title") + "\n");
                writer.write("Description: " + getTagText(item, "description") + "\n");
                writer.write("Link: " + getTagText(item, "link") + "\n");
                writer.write("Published: " + getTagText(item, "pubDate") + "\n");
                writer.write("-----------------------------------------------\n");
            }

        } catch (Exception e) {
            try {
                writer.write("Error parsing feed: " + e.getMessage() + "\n");
            } catch (IOException ignore) {}
        }
    }

    private String getTagText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) return "(none)";
        return nodes.item(0).getTextContent().trim();
    }
}
