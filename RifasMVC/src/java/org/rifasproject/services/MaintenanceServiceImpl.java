/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.rifasproject.domain.ArchivedLinkSet;
import org.rifasproject.domain.CommentOwner;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.domain.Movie;

/**
 *
 * @author char0n
 */
public class MaintenanceServiceImpl extends AbstractMaintenanceService {

    private static final Logger log = Logger.getLogger(MaintenanceServiceImpl.class);

    @Override
    public List<LinkSet> getUncheckedLinkSets(int firstResult, int maxResults) throws ServiceException {
        Query query;
        List<LinkSet> toReturn = null;
        try {
            query = this.linkSetDao.getCurrentSession().createQuery("from LinkSet l where l.lastChecked is null");
            query.setCacheable(true);
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResults);

            toReturn = (List<LinkSet>) query.list();
        } catch (Exception ex) {
            throw new ServiceException("Error getting unchecked LinkSet objects", ex);
        }

        return toReturn;
    }

    @Override
    public ArchivedLinkSet archiveLinkSet(LinkSet linkSet) throws ServiceException {

        // Cannot archive transient object
        if (linkSet.getId() == null) {
            throw new ServiceException("It is not possible to archive transient object");
        }

        // LinkSet -> ArchivedLinkSet conversion
        ArchivedLinkSet a = new ArchivedLinkSet();
        a.setUuid(linkSet.getUuid());
        a.setName(linkSet.getName());
        a.setDescription(linkSet.getDescription());
        a.setType(linkSet.getType());
        a.setSource(linkSet.getSource());
        a.setStorage(linkSet.getStorage());
        a.setCreated(linkSet.getCreated());
        a.setArchived(new Date());

        StringBuffer links = new StringBuffer();
        for (Link link : linkSet.getLinks()) {
            links.append(link.getUrl()+"\n");
        }
        a.setLinks(links.toString());

        Query query;
        try {
            // Saving ArchivedLinkSet,
            this.archiveDao.save(a);

            // Assigning comments from LinkSet to Archive
            query = this.commentsDao.getCurrentSession().createQuery("update Comment set itemId=:itemId, owner=:owner where itemId=:linkSetId");
            query.setParameter("itemId"        , a.getId());
            query.setParameter("owner"    , CommentOwner.ARCHIVE);
            query.setParameter("linkSetId"     , linkSet.getId());
            query.executeUpdate();

            // Deleting LinkSet
            this.linkSetDao.delete(linkSet, false);
        } catch (Exception ex) {
            throw new ServiceException("Error archiving LinkSet object", ex);
        } 
        
        return a;
    }

    @Override
    public List<LinkSet> getLinkSetsCheckedBefore(Date date, int firstResult, int maxResults) throws ServiceException {
        Query query;
        List<LinkSet> toReturn = null;
        try {
            query = this.linkSetDao.getCurrentSession().createQuery("from LinkSet l where l.lastChecked is not null and l.lastChecked <=:lastChecked");
            query.setCacheable(true);
            query.setParameter("lastChecked", date);
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResults);

            toReturn = (List<LinkSet>) query.list();
        } catch (Exception ex) {
            throw new ServiceException("Error getting LinkSet objects", ex);
        }

        return toReturn;
    }

    @Override
    public void checkUncheckedLinkSets(int firstResult, int maxResults) throws ServiceException {
        List<LinkSet> unchecked = this.getUncheckedLinkSets(firstResult, maxResults);
        for (LinkSet l : unchecked) {
            this.linkSetService.checkLinkSet(l);
            this.linkSetService.persist(l);
        }
    }

    @Override
    public void recheckOldLinkSets(Date date, int firstResult, int maxResults) throws ServiceException {
        List<LinkSet> unchecked = this.getLinkSetsCheckedBefore(date, firstResult, maxResults);
        for (LinkSet l : unchecked) {
            this.linkSetService.checkLinkSet(l);
            this.linkSetService.persist(l);
        }
    }

    @Override
    public int archiveDeletedLinkSets(int firstResult, int maxResults) throws ServiceException {
        Query query;
        Session session = this.linkSetDao.getCurrentSession();
        int processed = 0;
        List<LinkSet> linkSets;
        try {
            query = session.createQuery("select distinct l from LinkSet as l join l.links as lnk where l.lastChecked is not null and lnk.active is false");
            query.setCacheable(false);
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResults);
            linkSets  = query.list();
            processed = linkSets.size();
            for (LinkSet l: linkSets) {
                this.archiveLinkSet(l);
            }
            session.flush();
            session.clear();

        } catch (Exception ex) {
            throw new ServiceException("Error while archiving deleted LinkSets", ex);
        }

        return processed;
    }

    @Override
    public int cleanUnusedWebpages(int firstResult, int maxResults) throws ServiceException {
        Query query;        
        int processed = 0;
        Session session = this.webPageDao.getCurrentSession();
        List<Long> pages;
        try {
            query = session.createQuery("select w.id from WebPage as w left join w.linkSets as l group by w.id having count(l.id)=0");
            query.setCacheable(false);
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResults);
            pages     = query.list();
            processed = pages.size();

            for (Long pageId : pages) {
                this.webPageDao.deleteById(pageId);
            }
            session.flush();
            session.clear();
        } catch (Exception ex) {
            throw new ServiceException("Error cleaning empty WebPage objects", ex);
        }

        return processed;
    }

    @Override
    public List<LinkSet> getLinkSetsForSimtemap(int page) throws ServiceException {
        List<LinkSet> linkSets = null;
        Query query;
        int firstResult;
        int maxResult;
        int resultsPerPage = 100;

        try {
            firstResult = page * resultsPerPage;
            maxResult   = resultsPerPage;

            query = this.linkSetDao.getCurrentSession().createQuery("from LinkSet as l");
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResult);
            linkSets = query.list();
        } catch (Exception ex) {
            log.warn("Error loading LinkSet object for sitemap", ex);
            throw new ServiceException("Error loading LinkSet objects for sitemap", ex);
        }

        return linkSets;
    }

    @Override
    public List<Movie> getMoviesForSitemap(int page) throws ServiceException {
        List<Movie> movies = null;
        int firstResult;
        int maxResult;
        int resultsPerPage = 100;
        Query query;

        try {
            firstResult = page * resultsPerPage;
            maxResult   = resultsPerPage;

            query = this.movieDao.getCurrentSession().createQuery("from Movie as m");
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResult);
            movies = query.list();
        } catch (Exception ex) {
            log.warn("Error loading Movie object for sitemap", ex);
            throw new ServiceException("Error loading Movie object for sitemap", ex);
        }

        return movies;
    }
}
