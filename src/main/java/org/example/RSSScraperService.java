package org.example;

import java.io.IOException;
import java.util.List;

public interface RSSScraperService {

    /**
     * Scrapes multiple RSS feeds and writes combined results to a file.
     *
     * @param feedUrls       list of RSS feed URLs
     * @param outputFilePath path to the output file
     * @throws IOException if file writing or network issues occur
     */
    void scrapeFeeds(List<String> feedUrls, String outputFilePath) throws IOException;

}
