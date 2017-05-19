/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.rifasproject.domain.WebPage;
import org.rifasproject.services.DefaultUrlDownloader;
import org.rifasproject.services.ServiceException;
import org.rifasproject.services.UrlDownloader;

/**
 *
 * @author char0n
 */
public class MovieListDownloader {

    private String url = "http://www.movie2b.com/letter/[letter].html?page=[page]";
    private UrlDownloader downloader = new DefaultUrlDownloader();
    private Pattern titlePattern;
    private Pattern pagination;
    private Pattern yearPattern;

    public MovieListDownloader() {
        this.downloader.setUrlTimeout(10000);
        this.titlePattern = Pattern.compile("<span class=\"title\"><a href=\"http\\:\\/\\/[^\"]+\" title=\"[^\"]+\">([^\\<]+)</a>");
        this.pagination   = Pattern.compile("<a href=\"\\?page=[0-9]+\" class=\"btn\">[0-9]+</a>");
        this.yearPattern  = Pattern.compile("<a href=\"/year/[^\"]+\" title=\"[^\\<]+\">([0-9]+)</a>");
    }

    public String getUrl(String letter, int page) {
        return this.url.replace("[letter]", letter).replace("[page]", String.valueOf(page));
    }

    private String downloadPage(String url)  {
        WebPage page;
        try {
            page = this.downloader.download(url);
        } catch (ServiceException ex) {
            return "";
        }
        Matcher matcher = this.pagination.matcher(page.getContent());
        if (!matcher.find()) {
            throw new RuntimeException("No more pages for this letter");
        }

        return page.getContent();
    }

    private List<String> parsePage(String pageContent) {

        Matcher titleMatcher = this.titlePattern.matcher(pageContent);
        Matcher yearMatcher  = this.yearPattern.matcher(pageContent);
        List<String> titles = new ArrayList<String>();
        while (titleMatcher.find() && yearMatcher.find()) {
            titles.add(titleMatcher.group(1).trim()+" "+yearMatcher.group(1));
        }

        return titles;
    }

    public static void main(String[] args) throws ServiceException {
        MovieListDownloader mDownloader = new MovieListDownloader();
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File("out.txt")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        for (int i = 97; i <= 122; i++) {
            int j = 1;
            while (true) {
                String url = mDownloader.getUrl(String.valueOf((char) i), j);
                String pageContent;
                try {
                    pageContent = mDownloader.downloadPage(url);
                } catch (RuntimeException ex) {
                    break;
                }                
                List<String> titles    = mDownloader.parsePage(pageContent);
                for (String title : titles) {
                    System.out.println(title);
                    try {
                        writer.write(title);
                        writer.newLine();
                        writer.flush();
                    } catch (IOException ex) {
                    }
                    
                }
                j++;
            }
        }
        try {
            writer.close();
        } catch (IOException ex) { }
    }

}
