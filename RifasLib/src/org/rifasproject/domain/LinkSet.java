/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.SnowballPorterFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.apache.solr.analysis.StopFilterFactory;
import org.apache.solr.analysis.TrimFilterFactory;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.AnalyzerDefs;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;
import org.rifasproject.search.bridge.LinkSetLastCheckedFieldBridge;
import org.rifasproject.search.bridge.LinkSetLinksFieldBridge;
import org.rifasproject.search.bridge.LinkSetStorageFieldBridge;
import org.rifasproject.search.bridge.LinkSetTypeFieldBridge;

/**
 *
 * @author char0n
 */
@Indexed
@FullTextFilterDefs({
    @FullTextFilterDef(name="linkSetType"     , impl=org.rifasproject.search.filter.LinkSetTypeFilter.class),
    @FullTextFilterDef(name="linkSetStorage"  , impl=org.rifasproject.search.filter.InternetStorageFilter.class),
    @FullTextFilterDef(name="linkSetIsChecked", impl=org.rifasproject.search.filter.LinkSetIsCheckedFilter.class),
    @FullTextFilterDef(name="linkSetIsActive" , impl=org.rifasproject.search.filter.LinkSetIsActiveFilter.class)
})
@AnalyzerDefs ({
    @AnalyzerDef(name="defaultAnalyzer",
                 tokenizer=@TokenizerDef(factory=StandardTokenizerFactory.class),
                 filters= {
                    @TokenFilterDef(factory=LowerCaseFilterFactory.class),
                    @TokenFilterDef(factory=TrimFilterFactory.class),
                    @TokenFilterDef(factory=StopFilterFactory.class),
                    @TokenFilterDef(factory=SnowballPorterFilterFactory.class, params={@Parameter(name="language", value="English")})
                 }
    )
})
@Analyzer(definition="defaultAnalyzer")
public class LinkSet implements Serializable {
    @DocumentId
    private Long id;

    private String uuid;

    @Field(index=Index.TOKENIZED, store=Store.NO, boost=@Boost(2F))
    private String name;

    @Field(index=Index.TOKENIZED, store=Store.NO, boost=@Boost(1.5F))
    private String description;

    @IndexedEmbedded
    @Field(bridge=@FieldBridge(impl=LinkSetLinksFieldBridge.class), index=Index.UN_TOKENIZED, store=Store.YES)
    private Set<Link> links = new HashSet<Link>();

    @IndexedEmbedded
    private Set<Tag> tags = new HashSet<Tag>();

    @IndexedEmbedded
    private Set<WebPage> pages = new HashSet<WebPage>();

    @Field(bridge=@FieldBridge(impl=LinkSetTypeFieldBridge.class), index=Index.UN_TOKENIZED, store=Store.YES)
    private LinkSetType type;

    @Field(bridge=@FieldBridge(impl=LinkSetStorageFieldBridge.class), index=Index.UN_TOKENIZED, store=Store.YES)
    private InternetStorage storage;

    private LinkSetSource source;
    @Field(index=Index.UN_TOKENIZED)
    @DateBridge(resolution=Resolution.MINUTE)
    private Date created;
    @Field(bridge=@FieldBridge(impl=LinkSetLastCheckedFieldBridge.class), index=Index.UN_TOKENIZED, store=Store.YES)
    private Date lastChecked;

    private Set<Comment> comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Link> getLinks() {
        return links;
    }

    public void setLinks(Set<Link> links) {
        this.links = links;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        tag.getLinkSets().add(this);
        this.getTags().add(tag);
    }

    public Set<WebPage> getPages() {
        return pages;
    }

    public void setPages(Set<WebPage> pages) {
        this.pages = pages;
    }

    public void addPage(WebPage page)  {
        page.getLinkSets().add(this);
        this.pages.add(page);
    }

    public LinkSetType getType() {
        return type;
    }

    public void setType(LinkSetType type) {
        this.type = type;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(Date lastChecked) {
        this.lastChecked = lastChecked;
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

    public boolean isChecked()
    {
        return (this.getLastChecked() == null) ? false : true;
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
    
    public void addLink(Link link)
    {
        link.setLinkSet(this);
        this.links.add(link);
    }
    
    @Override
    public String toString()
    {
        return LinkSet.class.getSimpleName()+" ("+this.links.size()+")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LinkSet other = (LinkSet) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.uuid == null) ? (other.uuid != null) : !this.uuid.equals(other.uuid)) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.description == null) ? (other.description != null) : !this.description.equals(other.description)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (this.storage != other.storage) {
            return false;
        }
        if (this.source != other.source) {
            return false;
        }
        if (this.created != other.created && (this.created == null || !this.created.equals(other.created))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 17 * hash + (this.uuid != null ? this.uuid.hashCode() : 0);
        hash = 17 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 17 * hash + (this.description != null ? this.description.hashCode() : 0);
        hash = 17 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 17 * hash + (this.storage != null ? this.storage.hashCode() : 0);
        hash = 17 * hash + (this.source != null ? this.source.hashCode() : 0);
        hash = 17 * hash + (this.created != null ? this.created.hashCode() : 0);
        return hash;
    }
}
