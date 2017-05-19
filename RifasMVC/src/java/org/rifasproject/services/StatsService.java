/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import java.util.List;
import org.rifasproject.domain.Comment;
import org.rifasproject.domain.LibraryStatistics;
import org.rifasproject.domain.SearchKeyword;
import org.rifasproject.persistence.domain.ArchiveDao;
import org.rifasproject.persistence.domain.CommentsDao;
import org.rifasproject.persistence.domain.LinkSetDao;
import org.rifasproject.persistence.domain.LinksDao;
import org.rifasproject.persistence.domain.SearchKeywordDao;
import org.rifasproject.persistence.domain.TagsDao;
import org.rifasproject.persistence.domain.WebPageDao;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author root
 */
public interface StatsService {

    public void setSearchKeywordDao(SearchKeywordDao dao);
    public void setLinkSetDao(LinkSetDao dao);
    public void setLinksDao(LinksDao dao);
    public void setWebPageDao(WebPageDao dao);
    public void setTagsDao(TagsDao dao);
    public void setCommentsDao(CommentsDao dao);
    public void setArchiveDao(ArchiveDao dao);

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public List<SearchKeyword> getSearchCloud(int maxResults) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public List<SearchKeyword> getLatestSearches(int maxResults) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public LibraryStatistics generateLibraryStatistics(boolean replaceOld) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public LibraryStatistics getLibraryStatistics() throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public List<Comment> getLatestComments(int firstResult, int maxResults) throws ServiceException;
}
