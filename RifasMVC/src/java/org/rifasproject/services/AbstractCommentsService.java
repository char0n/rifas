/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import org.rifasproject.persistence.domain.ArchiveDao;
import org.rifasproject.persistence.domain.CommentsDao;
import org.rifasproject.persistence.domain.LinkSetDao;

/**
 *
 * @author char0n
 */
abstract public class AbstractCommentsService implements CommentsService {

    protected CommentsDao commentsDao;
    protected LinkSetService linkSetService;
    protected LinkSetDao linkSetDao;
    protected ArchiveService archiveService;
    protected ArchiveDao archiveDao;

    @Override
    public void setCommentsDao(CommentsDao dao) {
        this.commentsDao = dao;
    }

    @Override
    public void setLinkSetService(LinkSetService service) {
        this.linkSetService = service;
    }

    @Override
    public void setLinkSetDao(LinkSetDao dao) {
        this.linkSetDao = dao;
    }

    @Override
    public void setArchiveDao(ArchiveDao dao) {
        this.archiveDao = dao;
    }

    @Override
    public void setArchiveService(ArchiveService service) {
        this.archiveService = service;
    }
}