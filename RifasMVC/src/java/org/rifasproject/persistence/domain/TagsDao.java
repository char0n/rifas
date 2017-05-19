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
import org.hibernate.Session;
import org.rifasproject.domain.Tag;
import sk.mortality.persistence.AbstractHibernateSpringDao;
import sk.mortality.persistence.DaoException;

/**
 *
 * @author char0n
 */
public class TagsDao extends AbstractHibernateSpringDao<Tag, Long> {

    public Tag getByBinder(String binder) throws DaoException {
        
        Tag toReturn = null;
        try {
            Session session = this.getCurrentSession();
            Query query = session.createQuery("from Tag t WHERE t.binder=:binder");
            query.setString("binder", binder);
            query.setCacheable(true);
            toReturn = (Tag) query.uniqueResult();
        } catch (Exception ex) {
            throw new DaoException("Error getting Tag object by binder", ex);
        }

        return toReturn;
    }

    public Map<Long, List<Tag>> getTagsByLinkSetIds(Long[] ids) throws DaoException {
        Map<Long, List<Tag>> tags = new HashMap<Long, List<Tag>>();
        List results = null;
        try {
            Query query = this.getCurrentSession().createQuery("select ls.id, t from Tag as t join t.linkSets as ls where ls.id in(:linkSetIds)");
            query.setParameterList("linkSetIds", ids);
            query.setCacheable(true);
            results = query.list();

            Iterator i = results.iterator();
            Object[] row;
            while  (i.hasNext()) {
                row = (Object[]) i.next();
                Long linkSetId = (Long) row[0];
                Tag  tag       = (Tag)  row[1];
                if (!tags.containsKey(linkSetId)) {
                    tags.put(linkSetId, new ArrayList<Tag>());
                }
                tags.get(linkSetId).add(tag);
            }
        } catch (Exception ex) {
            throw new DaoException("Error getting Tag objects by LinkSet.id", ex);
        }

        return tags;
    }
}
