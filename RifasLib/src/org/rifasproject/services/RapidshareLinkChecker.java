/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.services;

import HTTPClient.CookieModule;
import HTTPClient.HTTPConnection;
import HTTPClient.HTTPResponse;
import HTTPClient.ModuleException;
import HTTPClient.NVPair;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.rifasproject.domain.InternetStorage;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.LinkSet;

/**
 *
 * @author char0n
 */
public class RapidshareLinkChecker implements LinkChecker {

    private static final int TIMEOUT             = 10000;
    private static final int THREAD_POOL_SIZE    = 15;
    private static Logger log                    = Logger.getLogger(RapidshareLinkChecker.class);
    private static Pattern activeLinkPattern     = Pattern.compile("\\<h1\\>FILE DOWNLOAD\\</h1\\>");
    private static Pattern activeLinkSizePattern = Pattern.compile("([0-9]+) KB\\</font\\>");
    private ExecutorService pool;

    @Override
    public Link check(Link link) throws LinkCheckerException {

        try {
            this.pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
            CheckTask task = new CheckTask(link);
            Future<Link> result = this.pool.submit(task);
            link = result.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            String msg = "Checking thread prematurely interruped";
            log.warn(msg, ex);
            throw new LinkCheckerException(msg, ex);
        } catch (TimeoutException ex) {
            String msg = "Checking thread reached its timeout, thread was terminated";
            log.warn(msg, ex);
            throw new LinkCheckerException(msg, ex);
        } catch (ExecutionException ex) {
            String msg = "Trying to get results from aborted download task";
            log.warn(msg, ex);
            throw new LinkCheckerException(msg, ex);
        } finally {
            this.pool.shutdownNow();
        }
        
        return link;
    }

    @Override
    public LinkSet check(LinkSet linkSet) throws LinkCheckerException {

        try {
            this.pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
            List<Future<Link>> threadsResults = new ArrayList<Future<Link>>();

            for (Link link : linkSet.getLinks()) {
                CheckTask task      = new CheckTask(link);
                Future<Link> result = this.pool.submit(task);
                threadsResults.add(result);
            }

            for (Future<Link> future : threadsResults) {
                future.get(TIMEOUT, TimeUnit.MILLISECONDS);
            }

            linkSet.setLastChecked(new Date());

        } catch (InterruptedException ex) {
            String msg = "Checking thread prematurely interruped";
            log.warn(msg, ex);
            throw new LinkCheckerException(msg, ex);
        } catch (TimeoutException ex) {
            String msg = "Checking thread reached its timeout, thread was terminated";
            log.warn(msg, ex);
            throw new LinkCheckerException(msg, ex);
        } catch (ExecutionException ex) {
            String msg = "Trying to get results from aborted download task";
            log.warn(msg, ex);
            throw new LinkCheckerException(msg, ex);
        } finally {
            this.pool.shutdownNow();
        }

        return linkSet;
    }
    
    @Override
    public InternetStorage getAssignedStorage() {
        return InternetStorage.RAPIDSHARE;
    }

    private class CheckTask implements Callable<Link>
    {
        private Link link;
        

        public CheckTask(Link link)
        {
            this.link = link;
        }

        private void yeald() throws InterruptedException {
            Thread.yield();
            if (Thread.currentThread().isInterrupted()) {
              throw new InterruptedException();
            }
        }

        @Override
        public Link call() throws LinkCheckerException {
            byte[] data;
            String content;
            int statusCode;
            HTTPConnection connection = null;

            try {
                URL url = new URL(this.link.getUrl());
                String host     = url.getHost();
                String file     = url.getFile();

                this.yeald();

                log.info("Checking status for: "+this.link.getUrl());
                connection = new HTTPConnection(host);
                connection.setTimeout(TIMEOUT);
                connection.setDefaultHeaders(new NVPair[] {new NVPair("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.7.12) Gecko/20050915 Firefox/1.0.7"), new NVPair("Referer", "http://test.sk")});
                connection.setAllowUserInteraction(false);
                connection.removeModule(CookieModule.class);
                HTTPResponse response = connection.Get(file);
                statusCode = response.getStatusCode();
                if (statusCode != 200) {
                    throw new IOException("Checker received invalid HTTP status code - "+statusCode);
                }
                data       = response.getData();
                content    = new String(data);
                data       = null;

                this.yeald();

                //Parsing the page for list status
                Matcher m = activeLinkPattern.matcher(content);
                this.link.setActive(m.find());

                // Parsing the page for link size
                if (this.link.isActive())
                {
                    Matcher m1 = activeLinkSizePattern.matcher(content);
                    if (m1.find() == true)
                    {
                        this.link.setSize(Integer.parseInt(m1.group(1)) * 1024);
                    }
                }

                this.yeald();
            } catch (ModuleException ex) {
                throw new LinkCheckerException(ex.getMessage(), ex);
            } catch (MalformedURLException ex) {
                throw new LinkCheckerException("Link checker obtained mallformed link to check", ex);
            } catch (IOException ex) {
                throw new LinkCheckerException("Could not check the link. Connection error.", ex);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(ex);
            } finally {
                if (connection != null) connection.stop();
            }

            return this.link;
        }

    }
}