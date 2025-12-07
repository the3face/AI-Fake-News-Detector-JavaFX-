import { writeFile, appendFile } from "fs/promises";
import { parseStringPromise } from "xml2js";

export class RSSScraper {
  async scrapeFeeds(feedUrls: string[], outputFilePath: string): Promise<void> {
    const header = `RSS Scrape Results - ${new Date().toISOString()}
=================================================

`;

    await writeFile(outputFilePath, header);

    for (const feedUrl of feedUrls) {
      await appendFile(outputFilePath, `Fetching feed: ${feedUrl}
-----------------------------------------------\n`);

      try {
        const xml = await this.fetch(feedUrl);
        await this.parseAndWrite(xml, outputFilePath);
      } catch (err: any) {
        await appendFile(outputFilePath, `Failed to retrieve feed: ${err.message}\n\n`);
      }

      await appendFile(outputFilePath, `\n\n`);
    }
  }

  private async fetch(url: string): Promise<string> {
    const res = await fetch(url);

    if (!res.ok) {
      throw new Error(`HTTP ${res.status} - ${res.statusText}`);
    }

    return await res.text();
  }

  private async parseAndWrite(xml: string, outputFilePath: string): Promise<void> {
    try {
      const json = await parseStringPromise(xml, {
        trim: true,
        explicitArray: false,
        mergeAttrs: true,
      });

      const channel = json?.rss?.channel;
      if (!channel) {
        await appendFile(outputFilePath, "Not a valid RSS feed.\n\n");
        return;
      }

      const title = channel.title ?? "(none)";
      await appendFile(outputFilePath, `Feed Title: ${title}\n\n`);

      const items = Array.isArray(channel.item) ? channel.item : [channel.item];

      for (const item of items) {
        await appendFile(
          outputFilePath,
          `Title: ${item.title ?? "(none)"}
Description: ${item.description ?? "(none)"}
Link: ${item.link ?? "(none)"}
Published: ${item.pubDate ?? "(none)"}
-----------------------------------------------
`
        );
      }
    } catch (err: any) {
      await appendFile(outputFilePath, `Error parsing feed: ${err.message}\n`);
    }
  }
}