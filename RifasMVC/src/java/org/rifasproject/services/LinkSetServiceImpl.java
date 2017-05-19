/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.search.FullTextSession;
import org.openpaste.services.Openpaste;
import org.rifasproject.domain.ArchivedLinkSet;
import org.rifasproject.domain.Comment;
import org.rifasproject.domain.CommentOwner;
import org.rifasproject.domain.InternetSearchEngine;
import org.rifasproject.domain.InternetStorage;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.domain.LinkSetSource;
import org.rifasproject.domain.LinkSetType;
import org.rifasproject.domain.SearchKeyword;
import org.rifasproject.domain.SearchResult;
import org.rifasproject.domain.Tag;
import org.rifasproject.domain.WebPage;
import org.rifasproject.util.LinkSetSortOrder;
import org.rifasproject.util.LinkSetTypeUtil;
import org.rifasproject.util.ResultSetFilter;
import sk.mortality.persistence.DaoException;
import org.rifasproject.util.SHA1;
import org.rifasproject.util.SearchSortUtil;

/**
 *
 * @author char0n
 */
public class LinkSetServiceImpl extends AbstractLinkSetService {

    private final Logger log = Logger.getLogger(LinkSetService.class);

    @Override
    public List<WebPage> searchOnInternet(String phrase, InternetSearchEngine engine, InternetStorage storage, int page) throws ServiceException {

        List<SearchResult> results;
        List<WebPage> pages;
        List<WebPage> parsedPages = null;

        try {
            this.searchEngines.get(engine).setStorage(storage);
            results     = this.searchEngines.get(engine).getResults(phrase, page);
            pages       = this.downloader.download(results);
            parsedPages = this.parsers.get(storage).parse(pages);

        } catch (ParsingException ex) {
            throw new ServiceException("Error parsing search engine results", ex);
        } catch (SearchEngineException ex) {
            throw new ServiceException("Error in search engine response", ex);
        }

        // Removing duplicate linksets in one webpage
        Set<String> UUIDs = new HashSet<String>();
        for (WebPage parsedPage : parsedPages) {
            Set<LinkSet> linkSets = new HashSet<LinkSet>();
            for (LinkSet linkSet : parsedPage.getLinkSets()) {
                String UUID = generateLinkSetUuid(linkSet);
                if (!UUIDs.contains(UUID)) {
                    UUIDs.add(UUID);
                    linkSet.setUuid(UUID);
                    linkSet.setCreated(new Date());
                    linkSet.setSource(LinkSetSource.INTERNET_SEARCH);
                    linkSets.add(linkSet);
                }
            }
            parsedPage.setLinkSets(linkSets);
        }


        return parsedPages;
    }

    @Override
    public List<WebPage> searchOnInternetAndPersist(String phrase, InternetSearchEngine engine, InternetStorage storage, int page) throws ServiceException {
        
        List<WebPage> parsedPages = this.searchOnInternet(phrase, engine, storage, page);

        for (WebPage parsedPage : parsedPages) {
            Set<LinkSet> linkSets = new HashSet<LinkSet>();
            for (LinkSet linkSet : parsedPage.getLinkSets()) {

                // Link validation
                Set<Link> links = (Set<Link>) ((TreeSet<Link>) linkSet.getLinks()).clone();
                links.clear();

                for (Link link : linkSet.getLinks()) {
                    if (this.linkValidator.isValidForSave(link)) {
                        links.add(link);
                    }


                }
                linkSet.setLinks(links);

                // LinkSet.name length updater
                if (linkSet.getName().length() > 80) {
                    linkSet.setName(linkSet.getName().substring(0, 77)+"...");
                }

                // LinkSet validation
                try {
                    // No links in linkset
                    if (linkSet.getLinks().size() == 0 ||
                        this.isRegisteredLinkSet(linkSet.getUuid()) ||
                        !this.linkSetValidator.isValidForSave(linkSet)) {
                        continue;
                    }
                } catch (ServiceException ex) {
                    log.warn("Error loading LinkSet object by Uuid", ex);
                    continue;
                }

                // Tag validations
                Set<Tag> tags = this.checkUniqueLinkSetTags(linkSet.getTags());
                linkSet.getTags().clear();
                for (Tag tag: tags) {
                    if (!this.tagValidator.isValidForSave(tag)) {
                        continue;
                    }

                    if (tag.getId() == null) {
                        this.tagsDao.saveOrUpdate(tag);
                    }
                    linkSet.addTag(tag);
                }
                LinkSetType type = LinkSetTypeUtil.getTypeByFileName(linkSet.getLinks().iterator().next().getUrl());
                linkSet.setType(type);
                linkSets.add(linkSet);
            }

            parsedPage.setLinkSets(linkSets);
            if (parsedPage.getLinkSets().size() != 0 && this.webPageValidator.isValidForSave(parsedPage)) {
                    this.webPageDao.saveOrUpdate(parsedPage);
            }
        }

        return parsedPages;
    }
    
    @Override
    public LinkSet persistLinkSetTree(LinkSet linkSet) throws ServiceException {

        // Link validation
        Set<Link> links = (Set<Link>) ((TreeSet<Link>) linkSet.getLinks()).clone();
        links.clear();

        for (Link link : linkSet.getLinks()) {
            if (this.linkValidator.isValidForSave(link)) {
                links.add(link);
            }
        }
        linkSet.setLinks(links);

        // Shortening LinkSet name
        if (linkSet.getName().length() > 80) {
            linkSet.setName(linkSet.getName().substring(0, 77)+"...");
        }

        // LinkSet validation
        try {
            if (linkSet.getLinks().size() == 0) {
                throw new ServiceException("LinkSet contains no Link objects");
            } else if (this.linkSetDao.getLinkSetByUuid(linkSet.getUuid()) != null) {
                throw new ServiceException("LinkSet with UUID("+linkSet.getUuid()+") alredy in database");
            } else if (this.linkSetValidator.isValidForSave(linkSet) == false) {
                throw new ServiceException("LinkSet contains invalid data");
            }
        } catch (DaoException ex) {
            log.warn("Error loading LinkSet object by Uuid", ex);
            throw new ServiceException(ex.getMessage(), ex);
        }

        // Tag validations
        try {
            Set<Tag> tags = this.checkUniqueLinkSetTags(linkSet.getTags());
            linkSet.getTags().clear();
            for (Tag tag: tags) {
                if (!this.tagValidator.isValidForSave(tag)) {
                    continue;
                }

                if (tag.getId() == null) {
                    this.tagsDao.saveOrUpdate(tag);
                }
                linkSet.addTag(tag);
            }
        } catch (Exception ex) {
            throw new ServiceException("Error saving Tag object", ex);
        }


        // LinkSetType determination
        LinkSetType type = LinkSetTypeUtil.getTypeByFileName(linkSet.getLinks().iterator().next().getUrl());
        linkSet.setType(type);

        // Saving entire LinkSet tree
        try {
            this.linkSetDao.save(linkSet);
        } catch (Exception ex) {
            throw new ServiceException("Error saving LinkSet object", ex);
        }

        return linkSet;
    }

    @Override
    public void persist(Tag tag) throws ServiceException {
        this.log.debug("Saving/Updating Tag object");
        try {
            this.tagsDao.saveOrUpdate(tag);
        } catch (Exception ex) {
            throw new ServiceException("Error saving/updating Tag object", ex);
        }
    }

    @Override
    public void persist(WebPage page) throws ServiceException {

        this.log.debug("Saving/Updating WebPage object");
        try {
            this.webPageDao.saveOrUpdate(page);
        } catch (Exception ex) {
            throw new ServiceException("Error saving/updating WebPage object", ex);
        }
    }

    @Override
    public void persist(LinkSet linkSet) throws ServiceException {

        this.log.debug("Saving/Updating LinkSet object");
        try {
            this.linkSetDao.saveOrUpdate(linkSet);
        } catch (Exception ex) {
            throw new ServiceException("Error saving/updating LinkSet object", ex);
        }
    }

    @Override
    public void initialLinkSetIndex() throws ServiceException {

        FullTextSession fulltextSession;
        int BATCH_SIZE = 300;
        ScrollableResults results;
        try {
            fulltextSession= this.linkSetDao.getCurrentFulltextSession();
            results = fulltextSession.createCriteria(LinkSet.class).setFetchSize(BATCH_SIZE).scroll(ScrollMode.FORWARD_ONLY);
            int index = 0;
            while (results.next()) {
                index++;
                fulltextSession.index(results.get(0));
                if (index % BATCH_SIZE == 0) {
                    fulltextSession.flushToIndexes();
                    fulltextSession.clear();
                }
            }
            results.close();
        } catch (Exception ex) {
            throw new ServiceException("Error indexing Rifas Project", ex);
        }

    }

    @Override
    public HibernateResult<LinkSet, Integer> searchInLibrary(String phrase, LinkSetType  type, InternetStorage storage, ResultSetFilter filter, int sortBy, LinkSetSortOrder order, int firstResult,  int maxResult) throws ServiceException {

        HibernateResult<LinkSet, Integer> result = null;

        try {
            result = this.linkSetDao.search(phrase, type, storage, filter, sortBy, order, firstResult, maxResult);
        } catch (DaoException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }

        return result;
    }

    @Override
    public Set<Tag> checkUniqueLinkSetTags(Set<Tag> tags) throws ServiceException {

        Set<Tag> modifTags = new HashSet<Tag>();
        Tag checkedTag;

        try {
            for (Tag tag: tags) {
                checkedTag = this.tagsDao.getByBinder(tag.getBinder());
                if (checkedTag != null) {
                    modifTags.add(checkedTag);
                } else {
                    Tag newTag = new Tag();
                    newTag.setBinder(tag.getBinder());
                    newTag.setCreated(tag.getCreated());
                    modifTags.add(newTag);
                }
            }
        } catch (DaoException ex) {
            throw new ServiceException("Something went wrong in TagsDao while checking Tag objects", ex);
        } catch (Exception ex) {
            throw new ServiceException("Something went wrong while checking Tab objects", ex);
        }

        return modifTags;
    }

    @Override
    public String generateLinkSetUuid(LinkSet linkSet) throws ServiceException {
        Set<Link> links = linkSet.getLinks();
        if (links.size() == 0) {
            throw new ServiceException("Cannot generate UUID for LinkSet containing no links");
        }

        StringBuffer linkString = new StringBuffer();
        for (Link link : links) {
            linkString.append(link.getUrl());
        }

        String UUID = null;
        try {
            UUID = SHA1.encode(linkString.toString());
        } catch (Exception ex) {
            throw new ServiceException("Error generationg LinkSet UUID", ex);
        }

        return UUID;
    }

    @Override
    public LinkSet loadLinkSet(Long id) throws ServiceException {

        LinkSet linkSet = null;
        try {
            linkSet = this.linkSetDao.load(id);
        } catch (Exception ex) {
            throw new ServiceException("Error loding LinkSet object", ex);
        }

        return linkSet;
    }

    @Override
    public LinkSet getLinkSetByUuid(String uuid) throws ServiceException {
        LinkSet linkSet = null;

        try {
            linkSet = this.linkSetDao.getLinkSetByUuid(uuid);
        } catch (DaoException ex) {
            throw new ServiceException("Error loading LinkSet object by UUID", ex);
        }

        return linkSet;
    }

    @Override
    public LinkSet checkLinkSet(LinkSet linkSet) throws ServiceException {

        if (linkSet.getStorage() == null) {
            throw new ServiceException("Cannot check LinkSet with unknown type");
        }

        LinkSet toReturn;
        toReturn = this.checkers.get(linkSet.getStorage()).check(linkSet);

        return toReturn;
    }

    @Override
    public Map<Long, List<Link>> getLinksByLinkSetIds(Long[] ids) throws ServiceException {
        Map<Long, List<Link>> links = null;
        try {
            links = this.linksDao.getLinksByLinkSetIds(ids);
        } catch (DaoException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }

        return links;
    }

    @Override
    public Map<Long, List<Tag>> getTagsByLinkSetIds(Long[] ids) throws ServiceException {
        Map<Long, List<Tag>> tags = null;
        try {
            tags = this.tagsDao.getTagsByLinkSetIds(ids);
        } catch (DaoException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }

        return tags;
    }

    @Override
    public Map<Long, List<WebPage>> getWebPagesByLinkSetIds(Long[] ids) throws ServiceException {
        Map<Long, List<WebPage>> pages = null;
        try {
            pages = this.webPageDao.getWebPagesByLinkSetIds(ids);
        } catch (DaoException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }

        return pages;
    }

    @Override
    public boolean addCommentToLinkSet(Comment c, LinkSet l) throws ServiceException {
        try {
            l.addComment(c);
            this.linkSetDao.saveOrUpdate(l);
        } catch (Exception ex) {
            throw new ServiceException("Error adding new Comment to LinkSet", ex);
        }

        return true;
    }

    @Override
    public Map<Long, Long> getCommentCountsByLinkSetIds(Long[] ids) throws ServiceException {
        Hashtable<Long, Long> table = new Hashtable<Long, Long>();
        Query query;
        List results;
        Iterator i;
        Object[] row;
        Long count;
        Long linkSetId;

        try {
            query = this.commentsDao.getCurrentSession().createQuery("select c.itemId, count(c) from Comment as c where c.itemId in (:linkSetIds) and c.owner=:owner group by c.itemId");
            query.setParameter("owner", CommentOwner.LINKSET);
            query.setParameterList("linkSetIds", ids);
            query.setCacheable(true);
            results = query.list();
            i = results.iterator();
            while (i.hasNext()) {
                row       = (Object[]) i.next();
                linkSetId = (Long) row[0];
                count     = (Long) row[1];
                table.put(linkSetId, count);
            }
            
        } catch (Exception ex) {
            throw new ServiceException("Error getting Comment counts by LinkSet ids", ex);
        }

        return table;
    }

    @Override
    public List<LinkSet> getLinkSetsFromStringData(String content) throws ServiceException {
        List<LinkSet> linkSets = this.parsers.get(InternetStorage.RAPIDSHARE).parse(content);

        return linkSets;
    }

    @Override
    public boolean addNewWebPageToLinkSet(WebPage newPage, LinkSet linkSet) throws ServiceException {
        for (WebPage p : linkSet.getPages()) {
            if (p.getUrl().equals(newPage.getUrl())) {
                return true;
            }
        }

        try {
            newPage = this.downloader.download(newPage.getUrl());
            if (newPage != null) {
                this.webPageDao.save(newPage);
                linkSet.addPage(newPage);
                this.linkSetDao.saveOrUpdate(linkSet);
                
                return true;
            }    
        } catch (Exception ex) {
            throw new ServiceException("Error saving new WebPage object", ex);
        }       

        return false;
    }

    @Override
    public boolean isRegisteredLinkSet(String UUID) throws ServiceException {
        
        Query query;
        try {
            query = this.linkSetDao.getCurrentSession().createQuery("from LinkSet as l where l.uuid=:uuid");
            query.setCacheable(false);
            query.setParameter("uuid", UUID);
            // Registered in library
            if (query.uniqueResult() != null) {
                log.debug("LinkSet Registered in library");
                return true;
            }

            // Check archive
            query = this.archiveDao.getCurrentSession().createQuery("from ArchivedLinkSet as a where a.uuid=:uuid");
            query.setCacheable(false);
            query.setParameter("uuid", UUID);
            // Registered in archive
            if (query.uniqueResult() != null) {
                log.debug("LinkSet Registered in archive");
                return true;
            }

            // LinkSet not registered in library neither in archive
            log.debug("Unregistered LinkSet");
            return false;

        } catch (Exception ex) {
            throw new ServiceException("Error checking registered LinkSet", ex);
        }
    }

    @Override
    public Openpaste exportLinkSet(String uuid) throws ServiceException {
        Openpaste api = new Openpaste();
        LinkSet l;
        Set<Link> links;
        StringBuffer export = new StringBuffer();
        StringBuffer desc   = new StringBuffer();
        try {
            l     = this.linkSetDao.getLinkSetByUuid(uuid);
            links = l.getLinks();
            for (Link link : links) {
                export.append(link.getUrl()+"\n");
            }
            desc.append("RIFAS (Rapid Internet File Allocation Service)\n");
            desc.append("RIFAS LinkSet export - "+links.size()+" links exported\n");
            desc.append("LinkSet URI - http://rifasproject.org/linksets/show/"+uuid+"\n");
            desc.append("Smart Rapidshare search engine\n");
            desc.append("http://rifasproject.org/\n");
            desc.append("QuidProQuo inc.");

            api.setCode(export.toString().trim());
            api.setAuthorName("RIFAS");
            api.setDescription(desc.toString());
            api.save();
        } catch (Exception ex) {
            throw new ServiceException("Error exporting LinkSet to openpaste.org", ex);
        }
        return api;
    }

    @Override
    public List<LinkSet> getRelatedLinkSets(LinkSet l, int firstResult, int maxResults) throws ServiceException {
        List<LinkSet> related = null;
        HibernateResult<LinkSet, Integer> result = this.searchInLibrary(l.getName(), l.getType(), l.getStorage(), ResultSetFilter.ALL, SearchSortUtil.getDefaultSortId(), LinkSetSortOrder.DESC, firstResult, maxResults);

        related = result.getResults(); 
        related.remove(l);

        return related;
    }

    @Override
    public List<LinkSet> getRelatedLinkSets(ArchivedLinkSet l, int firstResult, int maxResult) throws ServiceException {
        List<LinkSet> related = null;
        HibernateResult<LinkSet, Integer> result = this.searchInLibrary(l.getName(), l.getType(), l.getStorage(), ResultSetFilter.ALL, SearchSortUtil.getDefaultSortId(), LinkSetSortOrder.DESC, firstResult, maxResult);

        related = result.getResults();

        return related;
    }

    @Override
    public List<LinkSet> getRealtedLinkSets(String keyword, LinkSetType type, InternetStorage storage, int firstResult, int maxResults) throws ServiceException {
        HibernateResult<LinkSet, Integer> result = this.searchInLibrary(keyword, type, storage, ResultSetFilter.ALL, SearchSortUtil.getDefaultSortId(), LinkSetSortOrder.DESC, firstResult, maxResults);
        List<LinkSet> related = result.getResults();

        return related;
    }
}