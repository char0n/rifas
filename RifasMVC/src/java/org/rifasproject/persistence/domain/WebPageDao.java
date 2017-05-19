/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.persistence.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.domain.MimeType;
import org.rifasproject.domain.WebPage;
import sk.mortality.persistence.AbstractHibernateSpringDao;
import sk.mortality.persistence.DaoException;

/**
 *
 * @author char0n
 */
public class WebPageDao extends AbstractHibernateSpringDao<WebPage, Long> {

    public Map<Long, List<WebPage>> getWebPagesByLinkSetIds(Long[] ids) throws DaoException {
        Map<Long, List<WebPage>> pages = new HashMap<Long, List<WebPage>>();
        List results = null;

        try {
            Query query = this.getCurrentSession().createQuery("select ls.id, w.id, w.type, w.url from WebPage as w join w.linkSets as ls where ls.id in(:linkSetIds)");
            query.setParameterList("linkSetIds", ids);
            query.setCacheable(true);
            results = query.list();

            Iterator i = results.iterator();
            Object[] row;
            while (i.hasNext()) {
                row = (Object[]) i.next();
                WebPage page = new WebPage();
                Long linkSetId = (Long) row[0];
                if (!pages.containsKey(linkSetId)) {
                    pages.put(linkSetId, new ArrayList<WebPage>());
                }
                page.setId((Long) row[1]);
                page.setType((MimeType) row[2]);
                page.setUrl((String) row[3]);
                pages.get(linkSetId).add(page);
            }
        } catch (Exception ex) {
            throw new DaoException("Error getting WebPage objects by LinkSet.id collection", ex);
        }
        
        return pages;
    }

    public Long getLinkSetsByWebPageIdCount(Long webPageId) throws DaoException {
        org.hibernate.Query query = null;

        try {
            query = this.getCurrentSession().createQuery("select count(ls) from WebPage as p inner join p.linkSets as ls where p.id=:webPageId");
            query.setLong("webPageId", webPageId);
            query.setCacheable(true);
        } catch (Exception ex) {
            throw new DaoException("Error loading LinkSet object by WebPage.id", ex);
        }       

        return (Long) query.uniqueResult();
    }

    public List<LinkSet> getLinkSetsByWebPageId(Long webPageId, int firstResult, int maxResult) throws DaoException {
        org.hibernate.Query query = null;

        try {
            query = this.getCurrentSession().createQuery("select ls from WebPage as p inner join p.linkSets as ls where p.id=:webPageId");
            query.setLong("webPageId", webPageId);
            query.setCacheable(true);
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResult);
        } catch (Exception ex) {
            throw new DaoException("Error loading LinkSet object by WebPage.id", ex);
        }


        return (List<LinkSet>) query.list();
    }
}
