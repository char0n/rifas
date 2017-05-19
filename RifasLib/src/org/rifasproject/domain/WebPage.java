/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.rifasproject.search.bridge.WebPageContentFieldBridge;
import org.rifasproject.search.bridge.WebPageUrlFieldBridge;

/**
 *
 * @author char0n
 */
public class WebPage implements Serializable {

    private Long id;
    @Field(bridge=@FieldBridge(impl=WebPageContentFieldBridge.class), index=Index.TOKENIZED, store=Store.NO, boost=@Boost(1.4F))
    private String content;
    private MimeType type;
    @Field(bridge=@FieldBridge(impl=WebPageUrlFieldBridge.class), index=Index.TOKENIZED, store=Store.NO, boost=@Boost(1.4F))
    private String url;
    private SearchResult result;
    @ContainedIn
    private Set<LinkSet> linkSets = new HashSet<LinkSet>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MimeType getType() {
        return type;
    }

    public void setType(MimeType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SearchResult getResult() {
        return result;
    }

    public void setResult(SearchResult result) {
        this.result = result;
    }

    public Set<LinkSet> getLinkSets() {
        return linkSets;
    }

    public void setLinkSets(Set<LinkSet> linkSets) {
        this.linkSets = linkSets;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WebPage other = (WebPage) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.content == null) ? (other.content != null) : !this.content.equals(other.content)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if ((this.url == null) ? (other.url != null) : !this.url.equals(other.url)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 37 * hash + (this.content != null ? this.content.hashCode() : 0);
        hash = 37 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 37 * hash + (this.url != null ? this.url.hashCode() : 0);
        return hash;
    }
}
