/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.domain.WebPage;
import sk.mortality.persistence.DaoException;

/**
 *
 * @author char0n
 */
public class WebPageServiceImpl extends AbstractWebPageService {

    private static final Pattern pageTitlePattern    = Pattern.compile("(?i)(<title.*?>)(.+?)(</title>)");
    private static final Pattern pageDescPattern     = Pattern.compile("<meta name=[\"|']description[\"|'] content=[\"|'](.+?)[\"|'][>| ?\\/>]", Pattern.CASE_INSENSITIVE);
    private static final Pattern pageKeywordsPattern = Pattern.compile("<meta name=[\"|']keywords[\"|'] content=[\"|'](.+?)[\"|'][>| ?\\/>]", Pattern.CASE_INSENSITIVE);

    @Override
    public WebPage getWebPageById(Long id) throws ServiceException {

        WebPage page = null;

        try {
            page = this.webPageDao.load(id);
        } catch (Exception ex) {
            throw new ServiceException("Error loading WebPage object by id", ex);
        }

        return page;
    }

    @Override
    public HibernateResult<LinkSet, Long> getLinkSetsByWebPageId(Long id, int firstResult, int maxResult) throws ServiceException {
        HibernateResult<LinkSet, Long> toReturn = null;
        List<LinkSet> results                   = null;
        Long resultSize                         = 0L;

        try {
            results    = this.webPageDao.getLinkSetsByWebPageId(id, firstResult, maxResult);
            resultSize = this.webPageDao.getLinkSetsByWebPageIdCount(id);
            toReturn   = new HibernateResult<LinkSet, Long>(results, resultSize);
        } catch (DaoException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }

        return toReturn;
    }

    @Override
    public Map<Long, List<Link>> getLinksByLinkSetIds(Long[] ids) throws ServiceException {
        Map<Long, List<Link>> links = null;
        try {
            links = this.linksDao.getLinksByLinkSetIds(ids);
        } catch (DaoException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }

        return links;
    }

    @Override
    public Map<String, String> getWebPageProperties(WebPage page) throws ServiceException {
        Map<String, String> props = new HashMap<String, String>();

        Matcher m;
        m = pageTitlePattern.matcher(page.getContent());
        if (m.find()) props.put("title", m.group(2).trim());
        m = pageDescPattern.matcher(page.getContent());
        if (m.find()) props.put("description", m.group(1).trim());
        m = pageKeywordsPattern.matcher(page.getContent());
        if (m.find()) props.put("keywords", m.group(1).trim());

        return props;
    }
}
