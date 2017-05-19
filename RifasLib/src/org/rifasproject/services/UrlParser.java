/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.services;

import java.util.Comparator;
import java.util.List;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.domain.WebPage;
import org.rifasproject.util.RegexRepository;

/**
 *
 * @author root
 */
public interface UrlParser {

    public void setLinkRegex(RegexRepository linkRegex);
    public void setLinkDescRegex(RegexRepository linkDescRegex);
    public void setComparator(Comparator<Link> comparator);
    public RegexRepository getLinkRegex();
    public RegexRepository getLinkDescRegex();
    public Comparator<Link> getComparator();

    WebPage parse(WebPage page) throws ParsingException;
    List<WebPage> parse(List<WebPage> pages) throws ParsingException;
    List<LinkSet> parse(String data) throws ParsingException;
}
