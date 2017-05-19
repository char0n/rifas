/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc.sitemap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.rifasproject.domain.Movie;
import org.rifasproject.services.MaintenanceService;
import org.rifasproject.services.ServiceException;
import sk.mortality.sitemap.Sitemap;
import sk.mortality.sitemap.SitemapUrlItem;
import sk.mortality.sitemap.SitemapUrlItemChangeFrequency;

/**
 *
 * @author char0n
 */
public class ImdbTitlesSitemap implements Sitemap<Movie> {

    private MaintenanceService maintenanceService;
    private List<Movie> items;
    private int counter;
    private int page;

    public void setMaintenanceService(MaintenanceService service) {
        this.maintenanceService = service;
    }

    @Override
    public void setup() {
        this.counter = 0;
        this.page    = 0;

        try {
            this.items = this.maintenanceService.getMoviesForSitemap(this.page);
        } catch (ServiceException ex) {
            this.items = new ArrayList<Movie>();
        }
    }

    @Override
    public SitemapUrlItem convertToSitemapUrlItem(Movie m) {
        SitemapUrlItem sui = new SitemapUrlItem();
        try {
            sui.setUrlLocation("http://rifasproject.org/imdb/title/" + m.getId() + "/" + URLEncoder.encode(m.getTitle(), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            sui.setUrlLocation("http://rifasproject.org/imdb/title/" + m.getId() + "/" + m.getTitle());
        }
        sui.setPriority(0.7F);
        sui.setChangeFrequency(SitemapUrlItemChangeFrequency.MONTHLY);
        if (m.getUpdated() != null) {
            sui.setLastModificationDate(m.getUpdated());
        } else {
            sui.setLastModificationDate(m.getCreated());
        }

        return sui;
    }

    @Override
    public boolean hasNext() {
       if (this.counter < this.items.size()) {
           return true;
       } else {
           List<Movie> newItems;
            try {
                newItems = this.maintenanceService.getMoviesForSitemap(++this.page);
            } catch (ServiceException ex) {
                return false;
            }
           if (newItems.size() == 0) {
               return false;
           } else {
               this.items.clear();
               this.items   = newItems;
               this.counter = 0;
               return true;
           }
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