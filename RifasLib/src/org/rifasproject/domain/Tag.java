/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

/**
 *
 * @author char0n
 */
public class Tag implements Serializable {

    private Long id;
    @Field(index=Index.TOKENIZED, store=Store.NO, boost=@Boost(1.7F))
    private String binder;
    private Date created;
    @ContainedIn
    private Set<LinkSet> linkSets = new HashSet<LinkSet>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBinder() {
        return binder;
    }

    public void setBinder(String binder) {
        this.binder = binder;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
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
        final Tag other = (Tag) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.binder == null) ? (other.binder != null) : !this.binder.equals(other.binder)) {
            return false;
        }
        if (this.created != other.created && (this.created == null || !this.created.equals(other.created))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 17 * hash + (this.binder != null ? this.binder.hashCode() : 0);
        hash = 17 * hash + (this.created != null ? this.created.hashCode() : 0);
        return hash;
    }
}