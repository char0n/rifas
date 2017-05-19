/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.services;

import org.rifasproject.domain.WebPage;
import org.rifasproject.domain.SearchResult;
import java.util.List;

/**
 *
 * @author root
 */
public interface UrlDownloader {

    public WebPage download(SearchResult result);
    public List<WebPage> download(List<SearchResult> results);
    public WebPage download(String url) throws ServiceException;
    public void setUrlTimeout(int timeout);
    public int getUrlTimeout();
}
