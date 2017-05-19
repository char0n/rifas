/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import org.rifasproject.persistence.domain.ArchiveDao;
import org.rifasproject.persistence.domain.CommentsDao;
import org.rifasproject.persistence.domain.LinkSetDao;
import org.rifasproject.persistence.domain.MovieDao;
import org.rifasproject.persistence.domain.SearchKeywordDao;
import org.rifasproject.persistence.domain.WebPageDao;

/**
 *
 * @author char0n
 */
abstract public class AbstractMaintenanceService implements MaintenanceService {

    protected LinkSetDao linkSetDao;
    protected ArchiveDao archiveDao;
    protected SearchKeywordDao searchKeywordDao;
    protected WebPageDao webPageDao;
    protected LinkSetService linkSetService;
    protected CommentsDao commentsDao;
    protected MovieDao movieDao;


    @Override
    public void setArchiveDao(ArchiveDao archiveDao) {
        this.archiveDao = archiveDao;
    }

    @Override
    public void setLinkSetDao(LinkSetDao linkSetDao) {
        this.linkSetDao = linkSetDao;
    }

    @Override
    public void setSearchKeywordDao(SearchKeywordDao searchKeywordDao) {
        this.searchKeywordDao = searchKeywordDao;
    }

    @Override
    public void setWebPageDao(WebPageDao dao) {
        this.webPageDao = dao;
    }

    @Override
    public void setLinkSetService(LinkSetService service) {
        this.linkSetService = service;
    }

    @Override
    public void setCommentsDao(CommentsDao commentsDao) {
        this.commentsDao = commentsDao;
    }

    @Override
    public void setMovieDao(MovieDao movieDao) {
        this.movieDao = movieDao;
    }
}
