/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc.cron;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TimerTask;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.rifasproject.domain.InternetSearchEngine;
import org.rifasproject.domain.InternetStorage;
import org.rifasproject.services.LinkSetService;
import org.rifasproject.services.ServiceException;
import org.springframework.web.context.ServletContextAware;

/**
 *
 * @author char0n
 */
public class DownloadLinkSets extends TimerTask implements ServletContextAware {

    private static Logger log = Logger.getLogger(DownloadLinkSets.class);
    private ServletContext servletContext;
    private LinkSetService linkSetService;

    public void setLinkSetService(LinkSetService service) {
        this.linkSetService = service;
    }

    @Override
    public void setServletContext(ServletContext context) {
        this.servletContext = context;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        File movieExport = new File(this.servletContext.getRealPath("out.txt"));
        File movieImport = new File(this.servletContext.getRealPath("imported.txt"));

        // No more movies to import
        if (movieExport.length() == 0) {
            return;
        }

        try {
            reader = new BufferedReader(new FileReader(movieExport));
            writer = new BufferedWriter(new FileWriter(movieImport));

            String movie = reader.readLine().trim();

            log.info("Importing movie: "+movie);
            try {
                this.linkSetService.searchOnInternetAndPersist(movie, InternetSearchEngine.GOOGLE, InternetStorage.RAPIDSHARE, 1);
            } catch (ServiceException ex) {
                log.warn("Unable to process movie by LinkSetService", ex);
            }            

            String movieToProcess;
            while ((movieToProcess = reader.readLine()) != null) {
                if (!movieToProcess.trim().equals(movie)) {
                    writer.write(movieToProcess);
                    writer.newLine();
                }
            }

            movieExport.delete();
            movieImport.renameTo(new File(this.servletContext.getRealPath("out.txt")));

        } catch (FileNotFoundException ex) {
            log.warn("Files needed for importing LinkSets not found", ex);
        } catch (IOException ex) {
            log.warn("Something wroing with IO/Operations", ex);
        } finally {
            try {
                reader.close();
                writer.close();
            } catch (Exception ex) { }
        }

    }
}
