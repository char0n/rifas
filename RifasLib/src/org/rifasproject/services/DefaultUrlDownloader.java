/*
 * GPLv2 with Classpath Exception
 */
package org.rifasproject.services;

import HTTPClient.CookieModule;
import HTTPClient.HTTPConnection;
import HTTPClient.HTTPResponse;
import HTTPClient.NVPair;
import java.util.concurrent.ExecutionException;
import org.rifasproject.domain.WebPage;
import org.rifasproject.domain.SearchResult;
import org.rifasproject.domain.MimeType;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.log4j.Logger;

/**
 *
 * @author char0n
 */
public class DefaultUrlDownloader extends UrlDownloaderImpl {

    private static final int THREAD_POOL_SIZE = 15;
    private final Logger log                  = Logger.getLogger(DefaultUrlDownloader.class);
    private ExecutorService pool;
    @Override
    public WebPage download(SearchResult result) {

        DownloadTask task      = new DownloadTask(result);
        WebPage page           = null;

        try {
            this.pool              = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
            Future<WebPage> future = this.pool.submit(task);
            page = future.get(this.getUrlTimeout(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException ex) {
            log.warn("Download thread reached its timeout and was terminated", ex);
        } catch (InterruptedException ex) {
            log.warn("Download thread prematurely interruped", ex);
        } catch (ExecutionException ex) {
            log.warn("Trying to get results from aborted download task");
        } catch (Exception ex) {
            log.warn("Unknown exception while downloading page content", ex);
        } finally {
            this.pool.shutdownNow();
        }

        return page;
    }

    @Override
    public List<WebPage> download(List<SearchResult> results) {

        List<WebPage> pages                  = new ArrayList<WebPage>();
        List<Future<WebPage>> threadsResults = new ArrayList<Future<WebPage>>();

        try {
            this.pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
            for (SearchResult result : results) {
                DownloadTask task      = new DownloadTask(result);
                Future<WebPage> future = this.pool.submit(task);
                threadsResults.add(future);
            }

            WebPage webPage;
            for (Future<WebPage> future : threadsResults) {

                try {
                    webPage = future.get(this.getUrlTimeout(), TimeUnit.MILLISECONDS);
                    // WebPage is not downloadadable
                    if (webPage != null && webPage.getUrl() != null) {
                        pages.add(webPage);
                    }
                } catch (TimeoutException ex) {
                    log.warn("Download thread reached its timeout and was terminated", ex);
                } catch (InterruptedException ex) {
                    log.warn("Download thread prematurely interruped", ex);
                    continue;
                } catch (ExecutionException ex) {
                    log.warn("Trying to get results from aborted download task", ex);
                    continue;
                } catch (Exception ex) {
                    log.warn("Unknown exception while downloading page content", ex);
                    continue;
                }
            }

        } catch (Exception ex) {
            log.warn("Uknown exception while running url downloader", ex);
        } finally {
            this.pool.shutdownNow();
        }

        return pages;
    }

    @Override
    public WebPage download(String url) throws ServiceException {
        URL url1;

        try {
            url1 = new URL(url);
        } catch (MalformedURLException ex) {
            throw new ServiceException("Malformed URL to download", ex);
        }

        SearchResult sResult = new SearchResult();
        sResult.setUrl(url1.toString());
        sResult.setVisibleUrl(url1.toString());

        return this.download(sResult);
    }

    private class DownloadTask implements Callable<WebPage>
    {
        private SearchResult result;

        public DownloadTask(SearchResult result)
        {
            this.result = result;
        }

        private void yeald() throws InterruptedException {
            Thread.yield();
            if (Thread.currentThread().isInterrupted()) {
              throw new InterruptedException();
            }
        }

        @Override
        public WebPage call() throws Exception {

            WebPage webPage           = null;
            HTTPConnection connection = null;
            try {
                URL url         = new URL(this.result.getUrl());
                String host     = url.getHost();
                String file     = url.getFile();
                int port        = url.getPort();
                port            = (port == -1) ? 80 : port;

                this.yeald();

                log.info("Downloading page content for "+this.result.getUrl());
                connection = new HTTPConnection(host, port);
                connection.setTimeout(DefaultUrlDownloader.this.getUrlTimeout());
                connection.setDefaultHeaders(new NVPair[] {new NVPair("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.7.12) Gecko/20050915 Firefox/1.0.7"), new NVPair("Referer", "http://rifasproject.org")});
                connection.setAllowUserInteraction(false);
                connection.removeModule(CookieModule.class);
                HTTPResponse response = connection.Get(file);
                MimeType     mimeType = MimeHelper.get(response.getHeader("Content-Type"));
                String content        = response.getText();

                this.yeald();

                webPage = new WebPage();
                webPage.setType(mimeType);
                webPage.setContent(content);
                webPage.setResult(this.result);
                webPage.setUrl(this.result.getUrl());

                this.yeald();

            } catch (MalformedURLException ex){
                DefaultUrlDownloader.this.log.warn("Error downloading malformed URL", ex);
            } catch (IOException ex) {
                DefaultUrlDownloader.this.log.warn("Something went wrong with IO operations", ex);
            } catch (NoSuchFieldException ex) {
                DefaultUrlDownloader.this.log.warn("Page is of unknown Mime type", ex);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(ex);
            } finally {
                if (connection != null) connection.stop();
            }
            
            return webPage;
        }

    }
}