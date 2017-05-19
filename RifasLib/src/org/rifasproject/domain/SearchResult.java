/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.domain;

import java.io.Serializable;

/**
 *
 * @author char0n
 */
public class SearchResult implements Serializable {

    private String url;
    private String visibleUrl;
    private String title;
    private String content;
    private String query;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVisibleUrl() {
        return visibleUrl;
    }

    public void setVisibleUrl(String visibleUrl) {
        this.visibleUrl = visibleUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuery() {
        return this.query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
