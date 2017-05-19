/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import org.rifasproject.domain.ArchivedLinkSet;
import org.rifasproject.domain.Comment;
import org.rifasproject.persistence.domain.ArchiveDao;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author char0n
 */
public interface ArchiveService {

    public void setArchiveDao(ArchiveDao dao);

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public ArchivedLinkSet getArchivedLinkSetByUUID(String uuid) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=false)
    public boolean addCommentToArchivedLinkSet(Comment c, ArchivedLinkSet al) throws ServiceException;
}
