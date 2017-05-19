/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import org.hibernate.Query;
import org.rifasproject.domain.ArchivedLinkSet;
import org.rifasproject.domain.Comment;

/**
 *
 * @author char0n
 */
public class ArchiveServiceImpl extends AbstractArchiveService {

    @Override
    public ArchivedLinkSet getArchivedLinkSetByUUID(String uuid) throws ServiceException {
        ArchivedLinkSet al = null;
        Query query;
        try {
            query = this.archiveDao.getCurrentSession().createQuery("from ArchivedLinkSet as al where al.uuid=:uuid");
            query.setCacheable(true);
            query.setParameter("uuid", uuid);
            al = (ArchivedLinkSet) query.uniqueResult();
        } catch (Exception ex) {
            throw new ServiceException("Error getting ArchivedLinkSet by UUID", ex);
        }

        return al;
    }

    @Override
    public boolean addCommentToArchivedLinkSet(Comment c, ArchivedLinkSet al) throws ServiceException {

        try {
            al.addComment(c);
            this.archiveDao.saveOrUpdate(al);
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }

        return true;
    }

}
