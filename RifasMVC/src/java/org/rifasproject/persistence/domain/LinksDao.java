/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.persistence.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.rifasproject.domain.Link;
import sk.mortality.persistence.AbstractHibernateSpringDao;
import sk.mortality.persistence.DaoException;

/**
 *
 * @author char0n
 */
public class LinksDao extends AbstractHibernateSpringDao<Link, Long> {

    public Map<Long, List<Link>> getLinksByLinkSetIds(Long[] ids) throws DaoException {
        Map<Long, List<Link>> links = new HashMap<Long, List<Link>>();
        Criteria criteria;

        try {
            criteria = this.getCurrentSession().createCriteria(Link.class);
            criteria.createAlias("linkSet", "ls").add(Restrictions.conjunction().add(Restrictions.in("ls.id", ids)));
            criteria.setCacheable(true);
            
            List<Link> linkList = (List<Link>) criteria.list();
            for (Link link : linkList) {
                if (!links.containsKey(link.getLinkSet().getId())) {
                    links.put(link.getLinkSet().getId(), new ArrayList<Link>());
                }

                links.get(link.getLinkSet().getId()).add(link);
            }
        } catch (Exception ex) {
            throw new DaoException("Error getting Link objects by LinkSet.id[] collection", ex);
        }

        return links;
    }    
}
