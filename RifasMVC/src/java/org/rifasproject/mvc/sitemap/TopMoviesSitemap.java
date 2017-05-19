/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc.sitemap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.springframework.web.context.ServletContextAware;
import sk.mortality.sitemap.Sitemap;
import sk.mortality.sitemap.SitemapUrlItem;
import sk.mortality.sitemap.SitemapUrlItemChangeFrequency;

/**
 *
 * @author char0n
 */
public class TopMoviesSitemap implements Sitemap<String>, ServletContextAware {

    private ServletContext servletContext;
    private List<String> items;
    private static final Logger log = Logger.getLogger(TopMoviesSitemap.class);
    private int counter = 0;

    @Override
    public void setServletContext(ServletContext context) {
        this.servletContext = context;
    }

    @Override
    public void setup() {
        this.counter = 0;
        this.items   = new ArrayList<String>();
        File movieFile = new File(this.servletContext.getRealPath("out.txt"));
        if (!movieFile.exists()) return;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(movieFile));
            String line;
            while ((line = reader.readLine()) != null) {
                this.items.add(line);
            }
        } catch (FileNotFoundException ex) {
            log.warn("Movie file not found", ex);
        } catch (IOException ex) {
            log.warn("Unable to read from movie file", ex);
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (IOException ex) { }
        }
    }

    @Override
    public SitemapUrlItem convertToSitemapUrlItem(String movieTitle) {
        SitemapUrlItem sui = new SitemapUrlItem();
        try {
            sui.setUrlLocation("http://rifasproject.org/linksets/search/" + URLEncoder.encode(movieTitle, "UTF-8") + "/in/all/storage/rapidshare/filter/all");
        } catch (UnsupportedEncodingException ex) {
            sui.setUrlLocation("http://rifasproject.org/linksets/search/" + movieTitle + "/in/all/storage/rapidshare/filter/all");
        }
        sui.setPriority(0.5F);
        sui.setChangeFrequency(SitemapUrlItemChangeFrequency.WEEKLY);

        return sui;
    }

    @Override
    public boolean hasNext() {
        if (this.counter < this.items.size()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public SitemapUrlItem next() {
        return this.convertToSitemapUrlItem(this.items.get(this.counter++));
    }

    @Override
    public void remove() {
        this.items.remove(this.counter);
    }
}
