/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import java.util.Map;
import org.rifasproject.domain.InternetSearchEngine;
import org.rifasproject.domain.InternetStorage;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.domain.Tag;
import org.rifasproject.domain.WebPage;
import org.rifasproject.persistence.domain.ArchiveDao;
import org.rifasproject.persistence.domain.CommentsDao;
import org.rifasproject.persistence.domain.LinkSetDao;
import org.rifasproject.persistence.domain.LinksDao;
import org.rifasproject.persistence.domain.SearchKeywordDao;
import org.rifasproject.persistence.domain.TagsDao;
import org.rifasproject.persistence.domain.WebPageDao;
import sk.mortality.util.validation.Validator;

/**
 *
 * @author char0n
 */
public abstract class AbstractLinkSetService implements LinkSetService {

    protected Map<InternetSearchEngine, SearchEngine> searchEngines;
    protected UrlDownloader downloader;
    protected Map<InternetStorage, UrlParser> parsers;
    protected Map<InternetStorage, LinkChecker> checkers;
    protected LinkSetDao linkSetDao;
    protected ArchiveDao archiveDao;
    protected TagsDao tagsDao;
    protected WebPageDao webPageDao;
    protected LinksDao linksDao;
    protected SearchKeywordDao searchKeywordDao;
    protected CommentsDao commentsDao;
    protected Validator<LinkSet> linkSetValidator;
    protected Validator<WebPage> webPageValidator;
    protected Validator<Link> linkValidator;
    protected Validator<Tag> tagValidator;

    @Override
    public void setSearchEngines(Map<InternetSearchEngine, SearchEngine> engines) {
        this.searchEngines = engines;
    }

    @Override
    public void setDownloader(UrlDownloader downloader) {
        this.downloader = downloader;
    }

    @Override
    public void setParsers(Map<InternetStorage, UrlParser> parsers) {
        this.parsers = parsers;
    }

    @Override
    public void setCheckers(Map<InternetStorage, LinkChecker> checkers) {
        this.checkers = checkers;
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
    public void setTagsDao(TagsDao dao) {
        this.tagsDao = dao;
    }

    @Override
    public void setWebPageDao(WebPageDao dao) {
        this.webPageDao = dao;
    }

    @Override
    public void setLinksDao(LinksDao dao) {
        this.linksDao = dao;
    }

    @Override
    public void setSearchKeywordDao(SearchKeywordDao dao) {
        this.searchKeywordDao = dao;
    }

    @Override
    public void setLinkSetValidator(Validator<LinkSet> linkSetValidator) {
        this.linkSetValidator  = linkSetValidator;
    }

    @Override
    public void setWebPageValidator(Validator<WebPage> webPageValidator) {
        this.webPageValidator  = webPageValidator;
    }

    @Override
    public void setLinkValidator(Validator<Link> linkValidator) {
        this.linkValidator  = linkValidator;
    }

    @Override
    public void setTagValidator(Validator<Tag> tagValidator) {
        this.tagValidator  = tagValidator;
    }

    @Override
    public void setCommentsDao(CommentsDao dao) {
        this.commentsDao = dao;
    }
}