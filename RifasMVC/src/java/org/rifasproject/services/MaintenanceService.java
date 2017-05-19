/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import java.util.Date;
import java.util.List;
import org.rifasproject.domain.ArchivedLinkSet;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.domain.Movie;
import org.rifasproject.persistence.domain.ArchiveDao;
import org.rifasproject.persistence.domain.CommentsDao;
import org.rifasproject.persistence.domain.LinkSetDao;
import org.rifasproject.persistence.domain.MovieDao;
import org.rifasproject.persistence.domain.SearchKeywordDao;
import org.rifasproject.persistence.domain.WebPageDao;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author root
 */
public interface MaintenanceService {

    public void setLinkSetDao(LinkSetDao dao);
    public void setArchiveDao(ArchiveDao dao);
    public void setSearchKeywordDao(SearchKeywordDao dao);
    public void setWebPageDao(WebPageDao dao);
    public void setLinkSetService(LinkSetService service);
    public void setCommentsDao(CommentsDao commentDao);
    public void setMovieDao(MovieDao movieDao);

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public List<LinkSet> getUncheckedLinkSets(int firstResult, int maxResults) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=false)
    public void checkUncheckedLinkSets(int firstResult, int maxResults) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public List<LinkSet> getLinkSetsCheckedBefore(Date date, int firstResult, int maxResults) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=false)
    public void recheckOldLinkSets(Date age, int firstResult, int maxResults) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=false)
    public ArchivedLinkSet archiveLinkSet(LinkSet linkSet) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=false)
    public int archiveDeletedLinkSets(int firstResult, int maxResults) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=false)
    public int cleanUnusedWebpages(int firstResult, int maxResults) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public List<LinkSet> getLinkSetsForSimtemap(int page) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public List<Movie> getMoviesForSitemap(int page) throws ServiceException;
}
