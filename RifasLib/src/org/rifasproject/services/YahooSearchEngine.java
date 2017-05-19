/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import HTTPClient.CookieModule;
import HTTPClient.HTTPConnection;
import HTTPClient.HTTPResponse;
import HTTPClient.ModuleException;
import HTTPClient.NVPair;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.rifasproject.domain.InternetSearchEngine;
import org.rifasproject.domain.SearchResult;

/**
 *
 * @author char0n
 */
public class YahooSearchEngine extends SearchEngineImpl {

    private static Logger log = Logger.getLogger(YahooSearchEngine.class);
    private static final int PAGES_PER_REQUEST = 10;

    @Override
    public InternetSearchEngine getAssignedEngineType() {
        return InternetSearchEngine.YAHOO;
    }

    @Override
    public List<SearchResult> getResults(String query) throws SearchEngineException {
        return this.getResults(query, 1);
    }

    @Override
    public List<SearchResult> getResults(String query, Integer page) throws SearchEngineException {

        String source              = this.getSource().toString();
        String content             = null;
        StringBuilder uri          = new StringBuilder(source);
        HTTPConnection connection  = null;

        try {
            uri.append(URLEncoder.encode(query+" ", "UTF-8"));
            // Yahoo cannot process "i robot http://rapidshare.com" queries
            // It requires "*" character at the end of search query (no comment)
            uri.append(URLEncoder.encode(this.getStorage().getUrl()+"*", "UTF-8"));
            uri.append("?appid=quidproquoinc@yahoo.com&format=json");
            uri.append("&start=");
            uri.append(String.valueOf(--page*PAGES_PER_REQUEST));


            URL url = new URL(uri.toString());
            String host     = url.getHost();
            String file     = url.getFile();

            log.info("Connecting to Yahoo API: "+uri.toString());
            connection = new HTTPConnection(host);
            connection.setTimeout(2000);
            connection.setDefaultHeaders(new NVPair[] {new NVPair("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.7.12) Gecko/20050915 Firefox/1.0.7"), new NVPair("Referer", "http://rifasproject.org")});
            connection.setAllowUserInteraction(false);
            connection.removeModule(CookieModule.class);
            HTTPResponse response = connection.Get(file);
            content               = new String(response.getData());
            log.info("Reading Yahoo search results");
        } catch (ModuleException ex) {
            throw new SearchEngineException(ex.getMessage(), ex);
        } catch (MalformedURLException ex) {
            throw new SearchEngineException("Not valid API URL", ex);
        } catch (UnsupportedEncodingException ex) {
            throw new SearchEngineException("Not valid Encoding for URL escaping", ex);
        } catch (IOException ex) {
            throw new SearchEngineException("Cannot connect to API URL", ex);
        } finally {
            if (connection != null) connection.stop();
        }

        List<SearchResult> results = new ArrayList<SearchResult>();
        JSONObject response;
        try {
            log.info("Parsing API response");
            response = new JSONObject(content);
            JSONArray jsonResults = response.getJSONObject("ysearchresponse").getJSONArray("resultset_web");


            JSONObject jsonResult;
            for (int i = 0; i < jsonResults.length(); i++)
            {
                jsonResult          = jsonResults.getJSONObject(i);
                SearchResult result = new SearchResult();
                result.setUrl(jsonResult.getString("url"));
                result.setVisibleUrl(jsonResult.getString("url"));
                result.setTitle(jsonResult.getString("title").replaceAll("\\<.*?>", ""));
                result.setContent(jsonResult.getString("abstract").replaceAll("\\<.*?>", ""));
                result.setQuery(query);

                results.add(result);
            }
        } catch (JSONException ex) {
            throw new SearchEngineException("Server returned invalid JSON response", ex);
        }

        return results;
    }
}
