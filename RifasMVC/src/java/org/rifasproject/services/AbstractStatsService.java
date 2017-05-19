/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import org.rifasproject.persistence.domain.ArchiveDao;
import org.rifasproject.persistence.domain.CommentsDao;
import org.rifasproject.persistence.domain.LinkSetDao;
import org.rifasproject.persistence.domain.LinksDao;
import org.rifasproject.persistence.domain.SearchKeywordDao;
import org.rifasproject.persistence.domain.TagsDao;
import org.rifasproject.persistence.domain.WebPageDao;

/**
 *
 * @author char0n
 */
abstract public class AbstractStatsService implements StatsService {

    protected SearchKeywordDao searchKeywordDao;
    protected LinkSetDao linkSetDao;
    protected LinksDao linksDao;
    protected WebPageDao webPageDao;
    protected TagsDao tagsDao;
    protected CommentsDao commentsDao;
    protected ArchiveDao archiveDao;

    @Override
    public void setSearchKeywordDao(SearchKeywordDao dao) {
        this.searchKeywordDao = dao;
    }

    @Override
    public void setLinkSetDao(LinkSetDao dao) {
        this.linkSetDao = dao;
    }

    @Override
    public void setLinksDao(LinksDao dao) {
        this.linksDao = dao;
    }

    @Override
    public void setWebPageDao(WebPageDao dao) {
        this.webPageDao = dao;
    }

    @Override
    public void setTagsDao(TagsDao dao) {
        this.tagsDao = dao;
    }

    @Override
    public void setCommentsDao(CommentsDao dao) {
        this.commentsDao = dao;
    }

    @Override
    public void setArchiveDao(ArchiveDao dao) {
        this.archiveDao = dao;
    }
}
