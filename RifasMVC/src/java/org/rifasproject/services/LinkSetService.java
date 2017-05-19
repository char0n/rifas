/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.openpaste.services.Openpaste;
import org.rifasproject.domain.ArchivedLinkSet;
import org.rifasproject.domain.Comment;
import org.rifasproject.domain.InternetSearchEngine;
import org.rifasproject.domain.InternetStorage;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.domain.LinkSetType;
import org.rifasproject.domain.Tag;
import org.rifasproject.domain.WebPage;
import org.rifasproject.persistence.domain.ArchiveDao;
import org.rifasproject.persistence.domain.CommentsDao;
import org.rifasproject.persistence.domain.LinkSetDao;
import org.rifasproject.persistence.domain.LinksDao;
import org.rifasproject.persistence.domain.SearchKeywordDao;
import org.rifasproject.persistence.domain.TagsDao;
import org.rifasproject.persistence.domain.WebPageDao;
import org.rifasproject.util.LinkSetSortOrder;
import org.rifasproject.util.ResultSetFilter;
import org.springframework.transaction.annotation.Transactional;
import sk.mortality.util.validation.Validator;

/**
 *
 * @author char0n
 */
public interface LinkSetService {

    public void setSearchEngines(Map<InternetSearchEngine, SearchEngine> engines);
    public void setDownloader(UrlDownloader downloader);
    public void setParsers(Map<InternetStorage, UrlParser>  parsers);
    public void setCheckers(Map<InternetStorage, LinkChecker> checkers);
    public void setLinkSetDao(LinkSetDao dao);
    public void setArchiveDao(ArchiveDao dao);
    public void setTagsDao(TagsDao dao);
    public void setWebPageDao(WebPageDao dao);
    public void setLinksDao(LinksDao dao);
    public void setSearchKeywordDao(SearchKeywordDao dao);
    public void setCommentsDao(CommentsDao dao);
    public void setLinkSetValidator(Validator<LinkSet> validator);
    public void setWebPageValidator(Validator<WebPage> validator);
    public void setLinkValidator(Validator<Link> validator);
    public void setTagValidator(Validator<Tag> validator);

    // Searching linksets on the internet
    public List<WebPage> searchOnInternet(String phrase, InternetSearchEngine engine, InternetStorage storage, int page) throws ServiceException;

    // Searching linksets on the internet and persisting in database
    @Transactional(rollbackFor={ServiceException.class})
    public List<WebPage> searchOnInternetAndPersist(String phrase, InternetSearchEngine engine, InternetStorage storage, int page) throws ServiceException;

    // Persisting entire linkSet tree
    @Transactional(rollbackFor={ServiceException.class}, readOnly=false)
    public LinkSet persistLinkSetTree(LinkSet linkSet) throws ServiceException;

    public List<LinkSet> getLinkSetsFromStringData(String content) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=false)
    public boolean addNewWebPageToLinkSet(WebPage newPage, LinkSet linkSet) throws ServiceException;

    // Searching linksets in internal database
    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public HibernateResult<LinkSet, Integer> searchInLibrary(String phrase, LinkSetType  type, InternetStorage storage, ResultSetFilter filter, int sortBy, LinkSetSortOrder order, int firstResult,  int maxResult) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=false)
    public LinkSet checkLinkSet(LinkSet linkSet) throws ServiceException;

    public String generateLinkSetUuid(LinkSet linkSet) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class})
    public void persist(Tag tag) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class})
    public void persist(WebPage page) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class})
    public void persist(LinkSet linkSet) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public LinkSet loadLinkSet(Long id) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class})
    public void initialLinkSetIndex() throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public Set<Tag> checkUniqueLinkSetTags(Set<Tag> tags) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public LinkSet getLinkSetByUuid(String uuid) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public Map<Long, List<Link>> getLinksByLinkSetIds(Long[] ids) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public Map<Long, List<Tag>> getTagsByLinkSetIds(Long[] ids) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public Map<Long, List<WebPage>> getWebPagesByLinkSetIds(Long[] ids) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public Map<Long, Long> getCommentCountsByLinkSetIds(Long[] ids) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=false)
    public boolean addCommentToLinkSet(Comment c, LinkSet l) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public boolean isRegisteredLinkSet(String UUID) throws ServiceException;

    public Openpaste exportLinkSet(String uuid) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public List<LinkSet> getRelatedLinkSets(LinkSet l, int firstResult, int maxResults) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public List<LinkSet> getRelatedLinkSets(ArchivedLinkSet l, int firstResult, int maxResult) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public List<LinkSet> getRealtedLinkSets(String keyword, LinkSetType type, InternetStorage storage, int firstResult, int maxResults) throws ServiceException;
}
