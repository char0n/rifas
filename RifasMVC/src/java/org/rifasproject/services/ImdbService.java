/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.rifasproject.domain.InternetStorage;
import org.rifasproject.domain.Movie;
import org.rifasproject.persistence.domain.LinksDao;
import org.rifasproject.persistence.domain.MovieDao;
import org.rifasproject.persistence.domain.MoviePersonDao;
import org.springframework.transaction.annotation.Transactional;
import sk.mortality.util.imdb.Jmdb;
import sk.mortality.util.opensubtitles.OpensubtitlesException;
import sk.mortality.util.opensubtitles.OpensubtitlesService;
import sk.mortality.util.opensubtitles.OpensubtitlesXmlRpcServiceImpl;

/**
 *
 * @author root
 */
public interface ImdbService {

    public void setMovieDao(MovieDao dao);
    public void setMoviePersonDao(MoviePersonDao dao);
    public void setLinkSetService(LinkSetService service);
    public void setLinksDao(LinksDao dao);
    public Jmdb createNewImdbApiInstance();
    public OpensubtitlesService createNewOpensubtitlesApiInstance() throws OpensubtitlesException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=false)
    public Object[] searchImdbTitles(String keyword) throws ServiceException;
    @Transactional(rollbackFor={ServiceException.class}, readOnly=false)
    public Object[] searchImdbTitles(String keyword, int timeout) throws ServiceException;
    @Transactional(rollbackFor={ServiceException.class}, readOnly=false)
    public Object[] getImdbTitleData(int imdbId, InternetStorage storage, Locale subtitleLang) throws ServiceException;
    public Map<Integer, String> getMatchedTitles(String imdbTitle) throws ServiceException;
    public Map<Integer, String> getMatchedTitles(String imdbTitle, int timeout) throws ServiceException;
    @Transactional(rollbackFor={ServiceException.class}, readOnly=false)
    public Movie getTitleByImdbId(Integer imdbId) throws ServiceException;
    @Transactional(rollbackFor={ServiceException.class}, readOnly=false)
    public Movie getTitleByImdbId(Integer imdbId, int timeout) throws ServiceException;
    public Set<OpensubtitlesXmlRpcServiceImpl.Subtitle> getSubtitles(int imdbId, Locale language) throws ServiceException;
    public Set<OpensubtitlesXmlRpcServiceImpl.Subtitle> getAsyncSubtitles(int imdbId, Locale language) throws ServiceException;
    public Set<Movie> getLatestCoveredTitles(int limit) throws ServiceException;
}