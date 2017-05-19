/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import org.rifasproject.persistence.domain.LinksDao;
import org.rifasproject.persistence.domain.WebPageDao;

/**
 *
 * @author char0n
 */
abstract public class AbstractWebPageService implements WebPageService {

    protected WebPageDao webPageDao;
    protected LinksDao linksDao;

    @Override
    public void setWebPageDao(WebPageDao dao) {
        this.webPageDao = dao;
    }

    @Override
    public void setLinksDao(LinksDao dao) {
        this.linksDao = dao;
    }
}
