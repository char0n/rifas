/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.services;

import org.rifasproject.domain.SearchEngineType;
import java.net.URL;
import org.rifasproject.domain.InternetStorage;

/**
 *
 * @author char0n
 */
abstract public class SearchEngineImpl implements SearchEngine {

    private InternetStorage storage;
    private URL source;
    private String name;
    private String description;
    private SearchEngineType type;


    @Override
    public void setStorage(InternetStorage storage) {
        this.storage = storage;
    }

    @Override
    public InternetStorage getStorage()
    {
        return this.storage;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public SearchEngineType getType() {
        return type;
    }

    @Override
    public void setType(SearchEngineType type) {
        this.type = type;
    }

    @Override
    public URL getSource() {
        return this.source;
    }

    @Override
    public void setSource(URL source) {
        this.source = source;
    }
}
