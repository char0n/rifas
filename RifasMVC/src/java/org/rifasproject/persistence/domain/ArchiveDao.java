/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.persistence.domain;

import org.hibernate.Session;
import org.rifasproject.domain.ArchivedLinkSet;
import sk.mortality.persistence.AbstractHibernateSpringDao;
import sk.mortality.persistence.DaoException;

/**
 *
 * @author char0n
 */
public class ArchiveDao extends AbstractHibernateSpringDao<ArchivedLinkSet, Long> {

    public ArchivedLinkSet getArchivedLinkSetByUuid(String UUID) throws DaoException {
        org.hibernate.Query query = null;
        try {
            Session session = this.getCurrentSession();
            query = session.createQuery("from ArchivedLinkSet as a where a.uuid=:uuid");
            query.setString("uuid", UUID);
            query.setCacheable(true);
        } catch (Exception ex) {
            throw new DaoException("Error getting ArchivedLinkSet object by uuid", ex);
        }

        return (ArchivedLinkSet) query.uniqueResult();
    }
}
