/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc.sitemap;

import java.util.ArrayList;
import java.util.List;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.services.MaintenanceService;
import org.rifasproject.services.ServiceException;
import sk.mortality.sitemap.Sitemap;
import sk.mortality.sitemap.SitemapUrlItem;
import sk.mortality.sitemap.SitemapUrlItemChangeFrequency;

/**
 *
 * @author char0n
 */
public class LinkSetsSitemap implements Sitemap<LinkSet> {

    private MaintenanceService maintenanceService;
    private List<LinkSet> items;
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
            this.items = this.maintenanceService.getLinkSetsForSimtemap(this.page);
        } catch (ServiceException ex) {
            this.items = new ArrayList<LinkSet>();
        }
    }

    @Override
    public SitemapUrlItem convertToSitemapUrlItem(LinkSet l) {
        SitemapUrlItem sui = new SitemapUrlItem();
        sui.setUrlLocation("http://rifasproject.org/linksets/show/"+l.getUuid());
        sui.setPriority(0.8F);
        sui.setChangeFrequency(SitemapUrlItemChangeFrequency.MONTHLY);
        if (l.isChecked() == true) {
            sui.setLastModificationDate(l.getLastChecked());
        } else {
            sui.setLastModificationDate(l.getCreated());
        }

        return sui;
    }

    @Override
    public boolean hasNext() {
       if (this.counter < this.items.size()) {
           return true;
       } else {
           List<LinkSet> newItems;
            try {
                newItems = this.maintenanceService.getLinkSetsForSimtemap(++this.page);
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
