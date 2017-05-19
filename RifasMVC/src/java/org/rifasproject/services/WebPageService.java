/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import java.util.List;
import java.util.Map;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.WebPage;
import org.rifasproject.persistence.domain.LinksDao;
import org.rifasproject.persistence.domain.WebPageDao;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author root
 */
public interface WebPageService {

    public void setWebPageDao(WebPageDao dao);
    public void setLinksDao(LinksDao dao);

    public Map<String, String> getWebPageProperties(WebPage page) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public WebPage getWebPageById(Long id) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public HibernateResult getLinkSetsByWebPageId(Long id, int firstResult, int maxResult) throws ServiceException;

    @Transactional(rollbackFor={ServiceException.class}, readOnly=true)
    public Map<Long, List<Link>> getLinksByLinkSetIds(Long[] ids) throws ServiceException;
}
