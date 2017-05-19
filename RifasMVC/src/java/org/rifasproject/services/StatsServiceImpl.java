/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import net.spy.memcached.MemcachedClient;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.rifasproject.domain.Comment;
import org.rifasproject.domain.CommentOwner;
import org.rifasproject.domain.LibraryStatistics;
import org.rifasproject.domain.SearchKeyword;

/**
 *
 * @author char0n
 */
public class StatsServiceImpl extends AbstractStatsService {

    private static final Logger log = Logger.getLogger(StatsServiceImpl.class);

    @Override
    public List<SearchKeyword> getSearchCloud(int maxResults) throws ServiceException {
        List<SearchKeyword> keywords = null;
        Query query;
        try {
            query = this.searchKeywordDao.getCurrentSession().createQuery("from SearchKeyword sk order by sk.searchCount desc");
            query.setCacheable(true);
            query.setFirstResult(0);
            query.setMaxResults(maxResults);

            keywords = (List<SearchKeyword>) query.list();
        } catch (Exception ex) {
            throw new ServiceException("Error while getting search cloud statistics", ex);
        }

        return keywords;
    }

    @Override
    public List<SearchKeyword> getLatestSearches(int maxResults) throws ServiceException {
        List<SearchKeyword> keywords = null;
        Query query;
        try {
            query = this.searchKeywordDao.getCurrentSession().createQuery("from SearchKeyword sk order by sk.lastSeen desc");
            query.setCacheable(true);
            query.setFirstResult(0);
            query.setMaxResults(maxResults);

            keywords = (List<SearchKeyword>) query.list();
        } catch (Exception ex) {
            throw new ServiceException("Error while getting lastest searches statistics", ex);
        }

        return keywords;
    }

    @Override
    public LibraryStatistics generateLibraryStatistics(boolean replaceOld) throws ServiceException {
        MemcachedClient mc   = null;
        LibraryStatistics ls = null;
        Query query          = null;
        Long result;
        
        try {
            mc = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
            log.info("Rehydrating LibraryStatistics entity");
            ls = (LibraryStatistics) mc.get("libraryStatistics");

            // Generate statistics
            if (replaceOld == true | ls == null) {
                log.info("Generating Library statistics");
                ls = new LibraryStatistics();
                // How many LinkSets
                query  = this.linkSetDao.getCurrentSession().createQuery("select count(l) from LinkSet as l");
                query.setCacheable(false);
                result = (Long) query.uniqueResult();
                ls.addProperty("linkSets", result);

                // How many Links
                query  = this.linksDao.getCurrentSession().createQuery("select count(l) from Link as l");
                query.setCacheable(false);
                result = (Long) query.uniqueResult();
                ls.addProperty("links", result);
                

                // How many WebPages
                query  = this.webPageDao.getCurrentSession().createQuery("select count(w) from WebPage as w");
                query.setCacheable(false);
                result = (Long) query.uniqueResult();
                ls.addProperty("webPages", result);
                

                // How many Tags
                query  = this.tagsDao.getCurrentSession().createQuery("select count(t) from Tag as t");
                query.setCacheable(false);
                result = (Long) query.uniqueResult();
                ls.addProperty("tags", result);

                // How many Comments
                query  = this.commentsDao.getCurrentSession().createQuery("select count(c) from Comment as c");
                query.setCacheable(false);
                result = (Long) query.uniqueResult();
                ls.addProperty("comments", result);

                // How many checked LinkSets
                query  = this.linkSetDao.getCurrentSession().createQuery("select count(l) from LinkSet as l where l.lastChecked is not null");
                query.setCacheable(false);
                result = (Long) query.uniqueResult();
                ls.addProperty("checkedLinkSets", result);
                
                // How many unchecked LinkSets
                query  = this.linkSetDao.getCurrentSession().createQuery("select count(l) from LinkSet as l where l.lastChecked is null");
                query.setCacheable(false);
                result = (Long) query.uniqueResult();
                ls.addProperty("uncheckedLinkSets", result);

                // How many deleted LinkSets
                query  = this.linkSetDao.getCurrentSession().createQuery("select count(distinct l) from LinkSet as l join l.links as lnk where l.lastChecked is not null and lnk.active is false");
                query.setCacheable(false);
                result = (Long) query.uniqueResult();
                ls.addProperty("deletedLinkSets", result);

                // How many active LinkSets
                query  = this.linkSetDao.getCurrentSession().createQuery("select count(distinct l) from LinkSet as l join l.links as lnk where l.lastChecked is not null and lnk.active is true");
                query.setCacheable(false);
                result = (Long) query.uniqueResult();
                ls.addProperty("activeLinkSets", result);

                // How many archived LinkSets
                query  = this.archiveDao.getCurrentSession().createQuery("select count(a) from ArchivedLinkSet as a");
                query.setCacheable(false);
                result = (Long) query.uniqueResult();
                ls.addProperty("archivedLinkSets", result);

                // How many LinkSets added yesterday
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, -1);
                Date yesterday = cal.getTime();
                Date today     = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                yesterday  = format.parse(format.format(yesterday));
                today      = format.parse(format.format(today));
                query  = this.linkSetDao.getCurrentSession().createQuery("select count(l) from LinkSet as l where l.created > :yesterday and l.created < :today");
                query.setParameter("yesterday", yesterday);
                query.setParameter("today", today);
                query.setCacheable(false);
                result = (Long) query.uniqueResult();
                ls.addProperty("yesterdayLinkSets", result);

                // Store statistics for 24 hours
                log.info("Dehydrating LibraryStatistics entity");
                mc.set("libraryStatistics", 86400, ls).get();
            }

        } catch (IOException ex) {
            throw new ServiceException(ex);
        } catch (Exception ex) {
            throw new ServiceException(ex);
        } finally {
            if (mc != null) mc.shutdown();
        }

        return ls;
    }

    @Override
    public LibraryStatistics getLibraryStatistics() throws ServiceException {
        return this.generateLibraryStatistics(false);
    }

    @Override
    public List<Comment> getLatestComments(int firstResult, int maxResults) throws ServiceException {
        Query q;
        List<Comment> comments = null;
        try {
            q = this.commentsDao.getCurrentSession().createQuery("from Comment as c where c.owner=:owner order by c.created desc");
            q.setCacheable(true);
            q.setParameter("owner", CommentOwner.LINKSET);
            q.setFirstResult(firstResult);
            q.setMaxResults(maxResults);
            comments = (List<Comment>) q.list();
        } catch (Exception ex) {
            throw new ServiceException("Error getting latest Comments", ex);
        }

        return comments;
    }
}