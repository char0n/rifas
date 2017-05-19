/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import org.rifasproject.persistence.domain.LinksDao;
import org.rifasproject.persistence.domain.MovieDao;
import org.rifasproject.persistence.domain.MoviePersonDao;
import sk.mortality.util.imdb.Jmdb;
import sk.mortality.util.opensubtitles.OpensubtitlesException;
import sk.mortality.util.opensubtitles.OpensubtitlesService;
import sk.mortality.util.opensubtitles.OpensubtitlesXmlRpcServiceImpl;

/**
 *
 * @author char0n
 */
public abstract class AbstractImdbService implements ImdbService {
    
    protected MovieDao movieDao;
    protected MoviePersonDao moviePersonDao;
    protected LinkSetService linkSetService;
    protected LinksDao linkDao;

    @Override
    public void setMovieDao(MovieDao dao) {
        this.movieDao = dao;
    }

    @Override
    public void setMoviePersonDao(MoviePersonDao dao) {
        this.moviePersonDao = dao;
    }

    @Override
    public void setLinkSetService(LinkSetService service) {
        this.linkSetService = service;
    }

    @Override
    public void setLinksDao(LinksDao dao) {
        this.linkDao = dao;
    }

    @Override
    public Jmdb createNewImdbApiInstance() { return new Jmdb(); }
    @Override
    public OpensubtitlesService createNewOpensubtitlesApiInstance() throws OpensubtitlesException { return new OpensubtitlesXmlRpcServiceImpl(); }
}