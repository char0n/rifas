/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.services;

import org.rifasproject.domain.SearchEngineType;
import java.net.URL;
import java.util.List;
import org.rifasproject.domain.InternetSearchEngine;
import org.rifasproject.domain.InternetStorage;
import org.rifasproject.domain.SearchResult;

/**
 *
 * @author root
 */
public interface SearchEngine {

    public void setStorage(InternetStorage storage);
    public InternetStorage getStorage();
    public void setSource(URL source);
    public URL getSource();
    public void setName(String name);
    public void setDescription(String description);
    public String getName();
    public String getDescription();
    public SearchEngineType getType();
    public void setType(SearchEngineType type);
    public InternetSearchEngine getAssignedEngineType();

    public List<SearchResult> getResults(String query) throws SearchEngineException;
    public List<SearchResult> getResults(String query, Integer page) throws SearchEngineException;
}
