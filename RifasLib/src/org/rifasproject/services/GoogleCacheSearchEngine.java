/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.JSONException;
import java.util.List;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.rifasproject.domain.InternetSearchEngine;
import org.rifasproject.domain.SearchResult;

/**
 *
 * @author char0n
 */
public class GoogleCacheSearchEngine extends SearchEngineImpl {

    private static Logger log = Logger.getLogger(GoogleSearchEngine.class);
    private static final int PAGES_PER_REQUEST = 4;

    @Override
    public List<SearchResult> getResults(String query) throws SearchEngineException {
        return this.getResults(query, 1);
    }

    @Override
    public List<SearchResult> getResults(String query, Integer page) throws SearchEngineException {

        String source              = this.getSource().toString();
        StringBuilder uri          = new StringBuilder(source);
        StringBuilder content      = new StringBuilder();
        BufferedReader in          = null;

        try {
            uri.append("&q="+URLEncoder.encode(query+" ", "UTF-8"));
            uri.append(URLEncoder.encode(this.getStorage().getUrl(), "UTF-8"));
            uri.append("&rsz=large");
            uri.append("&start=");
            uri.append(String.valueOf(--page*PAGES_PER_REQUEST));

            log.info("Connecting to Google API: "+uri.toString());
            URL api = new URL(uri.toString());
            URLConnection connection = api.openConnection();
            connection.addRequestProperty("Referer", "http://www.mortality.sk");

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            log.info("Reading Google search results");
            String line;
            while ((line = in.readLine()) != null)
            {
                content.append(line);
            }
            log.info(content.toString());
        } catch (MalformedURLException ex) {
            throw new SearchEngineException("Not valid API URL", ex);
        } catch (UnsupportedEncodingException ex) {
            throw new SearchEngineException("Not valid Encoding for URL escaping", ex);
        } catch (IOException ex) {
            throw new SearchEngineException("Cannot connect to API URL", ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {  }
        }

        List<SearchResult> results = new ArrayList<SearchResult>();
        JSONObject response;
        try {
            log.info("Parsing API response");
            response = new JSONObject(content.toString());
            JSONArray jsonResults = response.getJSONObject("responseData").getJSONArray("results");


            JSONObject jsonResult;
            for (int i = 0; i < jsonResults.length(); i++)
            {
                jsonResult          = jsonResults.getJSONObject(i);
                SearchResult result = new SearchResult();
                result.setUrl(((jsonResult.getString("cacheUrl").equals("")) ? jsonResult.getString("unescapedUrl") : jsonResult.getString("cacheUrl")));
                result.setVisibleUrl("http://"+jsonResult.getString("visibleUrl"));
                result.setTitle(jsonResult.getString("title").replaceAll("\\<.*?>", ""));
                result.setContent(jsonResult.getString("content").replaceAll("\\<.*?>", ""));
                result.setQuery(query);

                results.add(result);
            }
        } catch (JSONException ex) {
            throw new SearchEngineException("Server returned invalid JSON response", ex);
        }

        return results;
    }

    @Override
    public InternetSearchEngine getAssignedEngineType() {
        return InternetSearchEngine.GOOGLE;
    }
}