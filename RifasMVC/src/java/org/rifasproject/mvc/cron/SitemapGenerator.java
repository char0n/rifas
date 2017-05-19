/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc.cron;

import java.io.File;
import java.util.TimerTask;
import javax.servlet.ServletContext;
import org.springframework.web.context.ServletContextAware;

/**
 *
 * @author char0n
 */
public class SitemapGenerator extends TimerTask implements ServletContextAware {

    private ServletContext servletContext;
    private sk.mortality.sitemap.SitemapGenerator sitemapGenerator;

    public void setSitemapGenerator(sk.mortality.sitemap.SitemapGenerator sitemapGenerator) {
        this.sitemapGenerator = sitemapGenerator;
    }

    @Override
    public void setServletContext(ServletContext context) {
        this.servletContext = context;
    }

    @Override
    public void run() {

        String path = new File(this.servletContext.getRealPath("sitemap")).getPath();

        this.sitemapGenerator.generate(path);
        this.sitemapGenerator.generateSitemapIndex(path, "rifasproject.org", "sitemap");
    }
}
