/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import java.net.InetSocketAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import net.spy.memcached.MemcachedClient;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.rifasproject.domain.InternetStorage;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.domain.LinkSetType;
import org.rifasproject.domain.Movie;
import org.rifasproject.domain.MovieCast;
import org.rifasproject.domain.MoviePerson;
import org.rifasproject.domain.MoviePersonType;
import org.rifasproject.domain.MovieProperty;
import org.rifasproject.domain.MoviePropertyType;
import org.rifasproject.util.SHA1;
import sk.mortality.util.imdb.JmdbException;
import sk.mortality.util.imdb.Jmdb;
import sk.mortality.util.opensubtitles.OpensubtitlesService;
import sk.mortality.util.opensubtitles.OpensubtitlesXmlRpcServiceImpl;
import sk.mortality.util.opensubtitles.OpensubtitlesXmlRpcServiceImpl.Subtitle;

/**
 *
 * @author char0n
 */
public class ImdbServiceImpl extends AbstractImdbService {

    public static final Logger log = Logger.getLogger(ImdbServiceImpl.class);

    public static final int IMDB_THREAD_POOL_SIZE          = 5;
    public static final int IMDB_THREAD_TIMEOUT            = 1500;
    public static final int OPENSUBTITLES_THREAD_POOL_SIZE = 5;
    public static final int OPENSUBTITLES_THREAD_TIMEOUT   = 1500;

    protected ExecutorService imdbPool          = Executors.newFixedThreadPool(IMDB_THREAD_POOL_SIZE);
    protected ExecutorService opensubtitlesPool = Executors.newFixedThreadPool(OPENSUBTITLES_THREAD_POOL_SIZE);

    private class ImdbWorker implements Callable<Jmdb> {

        private String imdbTitle;
        private Integer imdbId;

        public ImdbWorker(String imdbTitle) {
            this.imdbTitle = imdbTitle;
        }

        public ImdbWorker(int imdbId) {
            this.imdbId = imdbId;
        }

        @Override
        public Jmdb call() throws Exception {
            Jmdb imdbApi = ImdbServiceImpl.this.createNewImdbApiInstance();
            // Search by string
            if (this.imdbTitle != null) {
                imdbApi.search(this.imdbTitle);
            }
            // Search by imdb id
            if (this.imdbId != null) {
                imdbApi.search(this.imdbId);
            }
            
            return imdbApi;
        }
    }

    private class SubtitlesWorker implements Callable<Set<Subtitle>> {

        private int imdbId;
        private Locale subLanguage;
        Integer timeout;

        public SubtitlesWorker(int imdbId, Locale language) {
            this.imdbId      = imdbId;
            this.subLanguage = language;
        }

        public SubtitlesWorker(int imdbId, Locale language, int timeout) {
            this(imdbId, language);
            this.timeout = timeout;
        }

        @Override
        public Set<Subtitle> call() throws Exception {
            Locale apiLanguage = new Locale("en");
            OpensubtitlesService opensubtitlesApi = ImdbServiceImpl.this.createNewOpensubtitlesApiInstance();
            if (this.timeout != null) {
                opensubtitlesApi.setConnectionTimeout(this.timeout / 2);
                opensubtitlesApi.setReplyTimeout(this.timeout / 2);
            }
            opensubtitlesApi.logIn("", "", apiLanguage, "RIFAS");
            Set<Subtitle> subtitles = opensubtitlesApi.searchSubtitles(this.imdbId, this.subLanguage);

            return subtitles;
        }
    }

    /* composite service method */
    @Override
    public Object[] searchImdbTitles(String keyword) throws ServiceException {
        return this.searchImdbTitles(keyword, IMDB_THREAD_TIMEOUT);
    }

    @Override
    public Object[] searchImdbTitles(String keyword, int timeout) throws ServiceException {
        /* Object[0] = matches */
        /* Object[1] = first match; loaded movie */
        log.debug("Performing imdb title search");
        Object[] toReturn = new Object[2];
        Map<Integer, String> matches;
        int firstMatchId;
        Movie firstMatch = null;

        try {
            matches = this.getMatchedTitles(keyword, timeout);
            if (matches.size() > 0) {
                firstMatchId = matches.keySet().iterator().next();
                firstMatch   = this.getTitleByImdbId(firstMatchId, timeout);
            }
            toReturn[0] = matches;
            toReturn[1] = firstMatch;
        } catch (Throwable ex) {
            log.warn("Something went wrong while searching for imdb title", ex);
            throw new ServiceException(ex);
        }

        return toReturn;

    }

    /* composite service method */
    @Override
    public Object[] getImdbTitleData(int imdbId, InternetStorage storage, Locale subtitleLang) throws ServiceException {
        log.debug("Loading imdb title related data");
        /* Object[0] = movie data */
        /* Object[1] = related linksets */
        /* Object[2] = links for related linksets */
        /* Object[3] = subtitles related to imdb title */
        Object[] toReturn = new Object[4];
        Movie movie;
        List<LinkSet> relatedLinkSets;
        Long[] relatedLinkSetIds;
        Map<Long, List<Link>> relatedLinks = null;
        Set<OpensubtitlesXmlRpcServiceImpl.Subtitle> subtitles = null;

        try {
            movie   = this.getTitleByImdbId(imdbId);
            relatedLinkSets = this.linkSetService.getRealtedLinkSets(movie.getTitle(), LinkSetType.UNSPECIFIED, storage, 0, 10);
            if (relatedLinkSets.size() > 0) {
                relatedLinkSetIds = new Long[relatedLinkSets.size()];
                int index = 0;
                for (LinkSet l : relatedLinkSets) {
                    relatedLinkSetIds[index++] = l.getId();
                }
                relatedLinks = this.linkDao.getLinksByLinkSetIds(relatedLinkSetIds);
            }

            try {
                subtitles = this.getSubtitles(imdbId, subtitleLang);
            } catch (ServiceException ex) { /*ignore*/ }

            toReturn[0] = movie;
            toReturn[1] = relatedLinkSets;
            toReturn[2] = relatedLinks;
            toReturn[3] = subtitles;
        } catch (Throwable ex) {
            log.warn("Something went wrong while getting data for imdb title", ex);
            throw new ServiceException(ex);
        }

        return toReturn;
    }

    @Override
    public Map<Integer, String> getMatchedTitles(String imdbTitle) throws ServiceException {
        return this.getMatchedTitles(imdbTitle, IMDB_THREAD_TIMEOUT);
    }

    @Override
    public Map<Integer, String> getMatchedTitles(String imdbTitle, int timeout) throws ServiceException {
        Map<Integer, String> matches = null;
        MemcachedClient mc = null;
        String hashedKey;

        // No title
        if (imdbTitle == null) {
            return matches;
        }

        ImdbWorker imdbTask;
        Jmdb imdbApi;
        try {
            mc = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
            hashedKey = "imdbSearch"+SHA1.encode(imdbTitle);
            log.info("Hydrating imdb matches");
            matches   = (Map<Integer, String>) mc.get(hashedKey);

            if (matches == null) {
                imdbTask            = new ImdbWorker(imdbTitle);
                Future<Jmdb> future = this.imdbPool.submit(imdbTask);
                imdbApi             = future.get(timeout, TimeUnit.MILLISECONDS);
                matches             = imdbApi.getMatchedTitles();
                log.info("Dehydrating imdb matches");
                mc.set(hashedKey, 86400, matches).get();
            }
        } catch (Exception ex) {
            throw new ServiceException(ex);
        } finally {
            if (mc != null) mc.shutdown();
            imdbApi = null;
        }

        return matches;
    }

    @Override
    public Movie getTitleByImdbId(Integer imdbId) throws ServiceException {
        return this.getTitleByImdbId(imdbId, IMDB_THREAD_TIMEOUT);
    }

    @Override
    public Movie getTitleByImdbId(Integer imdbId, int timeout) throws ServiceException {
        Movie m = null;
        Jmdb imdbApi;
        try {

            m = this.movieDao.get(imdbId);
            // Update Movie item after two months
            if (m != null) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, -2);
                Date lastUpdated = m.getUpdated();
                Date treshold    = cal.getTime();

                if (lastUpdated.compareTo(treshold) > 0) {
                    return m;
                }
            }

            // Jmdb search
            ImdbWorker imdbTask = new ImdbWorker(imdbId);
            Future<Jmdb> future = this.imdbPool.submit(imdbTask);
            imdbApi             = future.get(timeout, TimeUnit.MILLISECONDS);
            if (imdbApi.getStatus().equals(Jmdb.Status.KO)) {
                throw new JmdbException("Title not found");
            }

            // Converting Imdb api response to Movie object
            m = new Movie();
            m.setId(imdbApi.getMovieID());
            m.setTitle(imdbApi.getTitle());
            m.setYear(imdbApi.getYear());
            m.setPlot(imdbApi.getPlot());
            m.setFullPlot(imdbApi.getFullPlot());
            m.setCoverData(imdbApi.getCoverData());
            Jmdb.Rating rating = imdbApi.getRating();
            m.setRating((rating != null) ? rating.getRating() : null);
            m.setVotes((rating != null) ? rating.getVotes() : null);
            m.setTagline(imdbApi.getTagline());
            m.setRuntime(imdbApi.getRuntime());
            m.setTrivia(imdbApi.getTrivia());
            m.setGoofs(imdbApi.getGoofs());
            m.setAwards(imdbApi.getAwards());
            m.setCreated(new Date());
            m.setUpdated(new Date());

            // Akas
            if (imdbApi.getAka() != null) {
                StringBuffer akas = new StringBuffer();
                for (String aka : imdbApi.getAka()) {
                    akas.append(aka);
                    akas.append(", ");
                } if (akas.toString().length() > 0) {
                    m.setAkas(akas.toString().substring(0, akas.toString().length() - 2));
                }
            }

            MovieCast cast;
            MoviePerson mp;
            // Actors
            if (imdbApi.getCast() != null) {
                for (Jmdb.Actor actor : imdbApi.getCast()) {
                    if ((mp = this.moviePersonDao.get(actor.getId())) == null) {
                        mp   = new MoviePerson();
                        mp.setId(actor.getId());
                        mp.setName(actor.getName());
                        this.moviePersonDao.save(mp);
                    }
                    mp.addMovie(m);
                    cast = new MovieCast();
                    cast.setMoviePerson(mp);
                    cast.setType(MoviePersonType.ACTOR);
                    cast.setCharacter(actor.getCharacter());
                    m.addCast(cast);
                }
            }

            // Directors
            if (imdbApi.getDirectors() != null) {
                mp = null;
                for (Jmdb.Director director : imdbApi.getDirectors()) {
                    if ((mp = this.moviePersonDao.get(director.getId())) == null) {
                        mp = new MoviePerson();
                        mp.setId(director.getId());
                        mp.setName(director.getName());
                        this.moviePersonDao.save(mp);
                    }
                    mp.addMovie(m);
                    cast = new MovieCast();
                    cast.setMoviePerson(mp);
                    cast.setType(MoviePersonType.DIRECTOR);
                    m.addCast(cast);
                }
            }

            // Writers
            if (imdbApi.getWriters() != null) {
                mp = null;
                for (Jmdb.Writer writer : imdbApi.getWriters()) {
                    if ((mp = this.moviePersonDao.get(writer.getId())) == null) {
                        mp   = new MoviePerson();
                        mp.setId(writer.getId());
                        mp.setName(writer.getName());
                        this.moviePersonDao.save(mp);
                    }
                    mp.addMovie(m);
                    cast = new MovieCast();
                    cast.setMoviePerson(mp);
                    cast.setType(MoviePersonType.WRITER);
                    m.addCast(cast);
                }
            }

            // Languages
            MovieProperty prop;
            if (imdbApi.getLanguage() != null) {
                for (String lang : imdbApi.getLanguage()) {
                    prop = new MovieProperty();
                    prop.setType(MoviePropertyType.LANGUAGE);
                    prop.setValue(lang);
                    m.addLanguage(prop);
                }
            }

            // Countries
            prop = null;
            if (imdbApi.getCountry() != null) {
                for (String country : imdbApi.getCountry()) {
                    prop = new MovieProperty();
                    prop.setType(MoviePropertyType.COUNTRY);
                    prop.setValue(country);
                    m.addCountry(prop);
                }
            }

            // Genres
            prop = null;
            if (imdbApi.getGenre() != null) {
                for (String genre : imdbApi.getGenre()) {
                    prop = new MovieProperty();
                    prop.setType(MoviePropertyType.GENRE);
                    prop.setValue(genre);
                    m.addGenre(prop);
                }
            }

            // Simple validation of Title, AKAs, Awards and Runtime
            if (m.getTitle().length() > 255)   m.setTitle(m.getTitle().substring(0, 252)+"...");
            if (m.getAkas() != null && m.getAkas().length() > 255)    m.setAkas(m.getAkas().substring(0, 252)+"...");
            if (m.getAwards() != null && m.getAwards().length() > 255)  m.setAwards(m.getAwards().substring(0, 252)+"...");
            if (m.getRuntime() != null && m.getRuntime().length() > 255) m.setRuntime(m.getRuntime().substring(0, 252)+"...");

            this.movieDao.saveOrUpdate(m);

        } catch (JmdbException ex) {
            throw new ServiceException(ex);
        } catch (Exception ex) {
            throw new ServiceException(ex);
        } finally {
            imdbApi = null;
        }

        return m;
    }

    @Override
    public Set<Subtitle> getSubtitles(int imdbId, Locale language) throws ServiceException {
        Set<Subtitle> subtitles = null;
        SubtitlesWorker subtitlesTask;
        
        try {
            subtitlesTask                = new SubtitlesWorker(imdbId, language);
            Future<Set<Subtitle>> future = this.opensubtitlesPool.submit(subtitlesTask);
            subtitles                    = future.get();
        } catch (Exception ex) {
            throw new ServiceException(ex);
        } 

        return subtitles;
    }

    @Override
    public Set<Subtitle> getAsyncSubtitles(int imdbId, Locale language) throws ServiceException {
        Set<Subtitle> subtitles     = null;
        SubtitlesWorker subtitlesTask;

        try {
            subtitlesTask                = new SubtitlesWorker(imdbId, language, 20000);
            Future<Set<Subtitle>> future = this.opensubtitlesPool.submit(subtitlesTask);
            subtitles                    = future.get(20000, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }

        return subtitles;
    }

    @Override
    public Set<Movie> getLatestCoveredTitles(int limit) throws ServiceException {
        Set<Movie> toReturn = null;
        Session session;
        Query query;
        List titles;
        try {
            session = this.movieDao.getCurrentSession();
            query   = session.createQuery("from Movie as m where m.coverData is not null order by m.created desc");
            query.setFirstResult(0);
            query.setMaxResults(limit);
            query.setCacheable(true);
            titles  = query.list();
            toReturn = new HashSet<Movie>(titles);
        } catch (Exception ex) {
            log.warn("Error getting last covered imdb titles", ex);
            throw new ServiceException("Error getting last covered imdb titles", ex);
        }

        return toReturn;
    }
}
