/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.log4j.Logger;
import org.rifasproject.domain.InternetStorage;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.domain.LinkSetType;
import org.rifasproject.domain.Tag;
import org.rifasproject.domain.WebPage;

/**
 *
 * @author char0n
 */
public class RapidshareUrlParser extends UrlParserImpl {

    private static final Logger log = Logger.getLogger(RapidshareUrlParser.class);
    private Pattern linkPattern;

    @Override
    public WebPage parse(WebPage page) throws ParsingException {

        log.info("Parsing rapidshare.com links from document");

        List<LinkSet> linkSets = this.parse(page.getContent());
        for (LinkSet l : linkSets) {
            l.addPage(page);
        }

        return page;
    }

    @Override
    public List<WebPage> parse(List<WebPage> pages) throws ParsingException {

        log.info("Parsing rapidshare.com links from document list");

        List<WebPage> parsedPages = new ArrayList<WebPage>();

        WebPage parsedPage;
        for (WebPage page : pages) {
            parsedPage = this.parse(page);
            parsedPages.add(parsedPage);
        }

        return parsedPages;
    }

    @Override
    public List<LinkSet> parse(String data) throws ParsingException {

        Matcher linkMatcher            = this.getLinkPattern().matcher(data);
        String linkURL;
        String fileName = null;
        Map<String, Set<Link>> links   = new HashMap<String, Set<Link>>();
        List<LinkSet> linkSets         = new ArrayList<LinkSet>();

        log.info("Parsing rapidshare.com links from string data");

        try {
            while (linkMatcher.find())
            {
                linkURL          = "http://"+linkMatcher.group(1).trim();
                fileName         = linkMatcher.group(2).toString();
                fileName         = this.cleanFileName(fileName);

                // initialize storage for linkset
                if (links.containsKey(fileName) != true) {
                    links.put(fileName, new HashSet<Link>());
                }

                if (linkURL.indexOf(fileName) != -1)
                {
                    Link link  = new Link();
                    link.setUrl(linkURL);
                    links.get(fileName).add(link);
                }
            }

            // Creating LinkSet objects
            for (String firstLinkFileName : links.keySet())
            {
                // No links, skip this linkset
                if (links.get(firstLinkFileName).size() == 0) {
                    continue;
                }

                TreeSet<Link> tree = new TreeSet<Link>(this.getComparator());
                tree.addAll(links.get(firstLinkFileName));

                LinkSet linkSet = new LinkSet();
                linkSet.setStorage(InternetStorage.RAPIDSHARE);
                linkSet.setType(LinkSetType.UNSPECIFIED);
                for (Link tmpLink : tree)
                {
                    tmpLink.setLinkSet(linkSet);
                }
                linkSet.setLinks(tree);

                String linkSetName = this.getLinkSetName(firstLinkFileName);

                Set<Tag> tags         = this.getDefaultTags(linkSetName);

                for (Tag tmpTag: tags) {
                    linkSet.addTag(tmpTag);
                }

                linkSet.setName(linkSetName);
                linkSets.add(linkSet);
            }
        } catch (Exception ex) {
            log.warn("Something went wrong with the regex pattern", ex);
            throw new ParsingException("Something went wrong with the regex pattern", ex);
        }

        return linkSets;
    }

    private Pattern getLinkPattern() {
        if (this.linkPattern == null) {
            this.linkPattern = Pattern.compile(this.getLinkRegex().regex());
        }
        return this.linkPattern;
    }
}