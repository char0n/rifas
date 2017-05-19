/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.domain;

import java.io.Serializable;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

/**
 *
 * @author char0n
 */
public class Link implements Serializable {
    private Long id;
    @Field(bridge=@FieldBridge(impl=org.rifasproject.search.bridge.LinkUrlFieldBridge.class), index=Index.TOKENIZED, store=Store.NO)
    private String url;
    private boolean active;
    // @TODO cast to Long
    private int size;
    @ContainedIn
    private LinkSet linkSet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LinkSet getLinkSet() {
        return linkSet;
    }

    public void setLinkSet(LinkSet linkSet) {
        this.linkSet = linkSet;
    }

    @Override
    public String toString()
    {
        return this.url.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        else if (obj == null) return false;
        else if (this.getClass() != obj.getClass()) return false;

        final Link link = (Link) obj;
        if (!this.url.toString().equals(link.getUrl())) return false;
        if (this.active != link.isActive()) return false;

        return true;
     }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + ((this.id != null) ? this.id.hashCode() : 0);
        hash = 89 * hash + (this.url != null ? this.url.hashCode() : 0);
        hash = 89 * hash + (this.active ? 1 : 0);
        hash = 89 * hash + (new Integer(this.size).hashCode());
        hash = 89 * hash + (this.linkSet != null ? this.linkSet.hashCode() : 0);
        return hash;
    }
}
