/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.rifasproject.domain.InternetStorage;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.domain.LinkSetType;
import org.rifasproject.domain.Movie;
import org.rifasproject.util.LinkSetTypeUtil;
import org.rifasproject.util.ResultSetFilter;
import twitter4j.AsyncTwitter;
import twitter4j.Status;
import twitter4j.TwitterException;


/**
 *
 * @author char0n
 */

@Aspect
public class TwitterService {

    private static final Logger log              = Logger.getLogger(TwitterService.class);
    private static final long MIN_TWITTING_DELAY = 86000;  // in miliseconds
    private static final long TWITTING_DELAY     = 360000; // in miliseconds
    private static final int TWITTER_TIMEOUT     = 500;    // in miliseconds

    private AsyncTwitter twitterApi;
    private Date lastSuccessfulTweet;
    private Date lastTweetRequest;
    private ExecutorService pool;

    public void setTwitterApi(AsyncTwitter api) {
        this.twitterApi = api;
        this.twitterApi.setHttpConnectionTimeout(TWITTER_TIMEOUT);
        this.twitterApi.setHttpReadTimeout(TWITTER_TIMEOUT);
        System.setProperty("twitter4j.http.connectionTimeout", String.valueOf(TWITTER_TIMEOUT));
        System.setProperty("twitter4j.http.readTimeout", String.valueOf(TWITTER_TIMEOUT));
    }

    @Aspect
    private static class Pointcut {

        @org.aspectj.lang.annotation.Pointcut("execution(* org.rifasproject.services.LinkSetServiceImpl.searchInLibrary(..))")
        private void librarySearch() { }
        @org.aspectj.lang.annotation.Pointcut("execution(* org.rifasproject.services.ImdbServiceImpl.searchImdbTitles(..))")
        private void imdbSearch() { }
        @org.aspectj.lang.annotation.Pointcut("execution(* org.rifasproject.services.ImdbServiceImpl.getImdbTitleData(..))")
        private void imdbTitle() { }
    }

    private static class TwitterTimeoutException extends Exception {

        public TwitterTimeoutException(String message) {
            super(message);
        }
    }

    private class TwitterTask implements Callable<Object> {

        private String status;

        public TwitterTask(String status) {
            this.status = status;
        }

        @Override
        public Object call() throws Exception {
            Status resp;

            synchronized (TwitterService.this) {
                try {
                    // Get rid of concurent twitting threads
                    if (TwitterService.this.catTweetAgain() == false) {
                        throw new TwitterTimeoutException("Tweeting skipped");
                    }
                    TwitterService.this.lastTweetRequest    = new Date();
                    resp = TwitterService.this.twitterApi.updateStatus(this.status);
                    TwitterService.log.debug("Successfully updated the status to ["+resp.getText()+"]");
                    TwitterService.this.lastSuccessfulTweet = new Date();
                } catch (TwitterException ex) {
                    TwitterService.log.warn("Unable to set new status", ex);
                } catch (TwitterTimeoutException ex) {
                    TwitterService.log.debug(ex.getMessage()); 
                } catch (Exception ex) {
                    TwitterService.log.warn("Uknown exception while twitting", ex);
                }
            }

            return new Object();
        }
    }

    @Around("org.rifasproject.services.TwitterService.Pointcut.librarySearch()")
    public HibernateResult<LinkSet, Integer> tweetLibrarySearch(ProceedingJoinPoint call) throws ServiceException {
        HibernateResult<LinkSet, Integer> toReturn = null;
        Object[] args;
        FutureTask<Object> task;
        try {
            args     = call.getArgs();
            toReturn = (HibernateResult<LinkSet, Integer>) call.proceed(args);
            try {
                // Don't process anything if delay still active
                if (this.catTweetAgain() == false) {
                    throw new TwitterTimeoutException("Tweeting of library search skipped");
                }

                log.debug("Tweeting library search");

                String phrase           = (String) args[0];                
                String tphrase          = ((phrase.length() > 90) ? phrase.substring(0 ,87)+"..." : phrase).replace("\"", "");
                LinkSetType type        = (LinkSetType) args[1];
                InternetStorage storage = (InternetStorage) args[2];
                ResultSetFilter filter  = (ResultSetFilter) args[3];
                String url              = "http://rifasproject.org/linksets/search/"+URLEncoder.encode(phrase, "UTF-8")+"/in/"+LinkSetTypeUtil.searchAcronymByLinkSetType(type)+"/storage/"+storage.name().toLowerCase()+"/filter/"+filter.value();
                String newStatus        = "Library search for: \""+tphrase+"\" ("+toReturn.getResultSize()+" matches) "+url;

                task = new FutureTask<Object>(new TwitterTask(newStatus));
                this.pool = Executors.newSingleThreadExecutor();
                this.pool.execute(task);
                task.get(TWITTER_TIMEOUT, TimeUnit.MILLISECONDS);
            } catch (Throwable ex) { log.debug("Tweeting of library search skipped", ex); }

        } catch (Throwable ex) {
            throw new ServiceException(ex);
        } finally {
            this.pool.shutdownNow();
        }

        return toReturn;
    }

    @Around("org.rifasproject.services.TwitterService.Pointcut.imdbSearch()")
    public Object[] tweetImdbSearch(ProceedingJoinPoint call) throws ServiceException {
        Object[] toReturn = null;
        Object[] args;
        FutureTask<Object> task;
        Map<Integer, String> matches;

        try {
            args     = call.getArgs();
            toReturn = (Object[]) call.proceed(args);
            
            try {
                // Don't process anything if delay still active
                if (this.catTweetAgain() == false) {
                    throw new TwitterTimeoutException("Tweeting of imdb search skipped");
                }
                matches           = (Map<Integer, String>) toReturn[0];
                String title      = (String) args[0];
                String tphrase    = ((title.length() > 90) ? title.substring(0, 87)+"..." : title).replace("\"", "");
                String url        = "http://rifasproject.org/imdb/search/"+URLEncoder.encode(title, "UTF-8");
                String newStatus  = "Imdb search for: \""+tphrase+"\" ("+matches.size()+" matches) "+url;

                task = new FutureTask<Object>(new TwitterTask(newStatus));
                this.pool = Executors.newSingleThreadExecutor();
                this.pool.execute(task);
                task.get(TWITTER_TIMEOUT, TimeUnit.MILLISECONDS);
            } catch (Throwable ex) {
                log.debug("Tweeting of imdb search skipped", ex);
            }
        } catch (Throwable ex) {
            throw new ServiceException(ex);
        } finally {
                this.pool.shutdown();
        }

        return toReturn;
    }

    @Around("org.rifasproject.services.TwitterService.Pointcut.imdbTitle()")
    public Object[] tweetImdbTitle(ProceedingJoinPoint call) throws ServiceException {
        Object[] toReturn = null;
        Object[] args;
        FutureTask<Object> task;

        try {
            args     = call.getArgs();
            toReturn = (Object[]) call.proceed(args);

            try {
                // Don't process anything if delay still active
                if (this.catTweetAgain() == false) {
                    throw new TwitterTimeoutException("Tweeting of imdb data loading skipped");
                }
                Movie movie      = (Movie) toReturn[0];
                String imdbTitle = movie.getTitle();
                String tphrase   = ((imdbTitle.length() > 90) ? imdbTitle.substring(0, 87)+"..." : imdbTitle).replace("\"", "");
                String url       = "http://rifasproject.org/imdb/title/"+movie.getId()+"/"+URLEncoder.encode(imdbTitle, "UTF-8");
                String newStatus = "Imdb title view for: \""+tphrase+" ("+movie.getYear()+")\" "+url;

                task = new FutureTask<Object>(new TwitterTask(newStatus));
                this.pool = Executors.newSingleThreadExecutor();
                this.pool.execute(task);
                task.get(TWITTER_TIMEOUT, TimeUnit.MILLISECONDS);
            } catch (Throwable ex) {
                log.debug("Tweeting of imdb title view skipped", ex);
            }
        } catch (Throwable ex) {
            throw new ServiceException(ex);
        } finally {
                this.pool.shutdown();
        }

        return toReturn;
    }

    private boolean catTweetAgain() {
        // First Tweet
        if (this.lastSuccessfulTweet == null && this.lastTweetRequest == null) {
            log.debug("No previous tweets, tweeting enabled");
            return true;
        }

        // Current timestamp
        long curTime = (new Date()).getTime();

        // Check tweet min delay
        if (this.lastTweetRequest != null) {
            long lastTweetRequestTime    = this.lastTweetRequest.getTime();
            if (curTime - lastTweetRequestTime <= TwitterService.MIN_TWITTING_DELAY) {
                log.debug("Request count per period excedeed, tweeting disabled");
                return false;
            }
        }

        // Check tweet delay
        boolean toReturn;
        long lastSuccessfulTweetTime = this.lastSuccessfulTweet.getTime();
        
        if (curTime - lastSuccessfulTweetTime > TwitterService.TWITTING_DELAY) {
            log.debug("Twitting delay valid, tweeting enabled");
            toReturn = true;
        } else {
            log.debug("Twitting delay invalid, tweeting enabled");
           toReturn = false;
        }

        return toReturn;
    }
}