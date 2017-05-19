/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author char0n
 */
public class ArchivedLinkSet implements Serializable {

    private Long id;
    private String uuid;
    private String name;
    private String description;
    private LinkSetType type;
    private InternetStorage storage;
    private LinkSetSource source;
    private Date created;
    private Date archived;
    private String links;
    private Set<Comment> comments;

    public Date getArchived() {
        return archived;
    }

    public void setArchived(Date archived) {
        this.archived = archived;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkSetSource getSource() {
        return source;
    }

    public void setSource(LinkSetSource source) {
        this.source = source;
    }

    public InternetStorage getStorage() {
        return storage;
    }

    public void setStorage(InternetStorage storage) {
        this.storage = storage;
    }

    public LinkSetType getType() {
        return type;
    }

    public void setType(LinkSetType type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment c) {
        this.comments.add(c);
    }
}
