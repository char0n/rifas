/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc.cron;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author char0n
 */
public class PeriodicSitemapGeneratorJob extends QuartzJobBean {

    private sk.mortality.sitemap.SitemapGenerator sitemapGenerator;
    private static final Logger log = Logger.getLogger(PeriodicSitemapGeneratorJob.class);

    public void setSitemapGenerator(sk.mortality.sitemap.SitemapGenerator sitemapGenerator) {
        this.sitemapGenerator = sitemapGenerator;
    }
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        ServletContext sc         = wac.getServletContext();

        try {
            String path = new File(sc.getRealPath("sitemap")).getPath();

            log.debug("Generating sitemap");
            this.sitemapGenerator.generate(path);
            this.sitemapGenerator.generateSitemapIndex(path, "rifasproject.org", "sitemap");
            try {
                log.debug("Pinging search engines");
                this.sitemapGenerator.pingSearchEngines(new URL("http://rifasproject.org/sitemaps"));
            } catch (MalformedURLException ex) { /* ignore */  }
        } catch (Throwable th) {
            log.warn("Error while running periodic sitemap generation", th);
            throw new JobExecutionException("Error while running periodic sitemap generation", th);
        }
    }
}