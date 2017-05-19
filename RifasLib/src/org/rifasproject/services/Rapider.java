/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.services;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.domain.SearchResult;
import org.rifasproject.domain.Tag;
import org.rifasproject.domain.WebPage;
import org.rifasproject.util.LinkComparator;
import org.rifasproject.util.LinkSizeUtil;
import org.rifasproject.util.RegexRepository;

/**
 *
 * @author root
 */
public class Rapider {

    private SearchEngine engine;
    private UrlDownloader downloader;
    private UrlParser parser;

    private static Logger log = Logger.getLogger(Rapider.class);

    static
    {
        //URL config = Loader.getResource("log4j.xml");
        //DOMConfigurator.configure(config);
    }

    public void setDownloader(UrlDownloader downloader) {
        this.downloader = downloader;
    }

    public void setEngine(SearchEngine engine) {
        this.engine = engine;
    }

    public void setParser(UrlParser parser) {
        this.parser = parser;
    }

    public List<LinkSet> run(String query, Integer page) throws RapiderException
    {
        try {
            List<SearchResult> results = this.engine.getResults(query, page);
            List<WebPage> pages       = this.downloader.download(results);
            List<WebPage> parsedPages = this.parser.parse(pages);
            
            List<LinkSet> linkSets = new ArrayList<LinkSet>();
            for (WebPage parsedPage : parsedPages) {
                for (LinkSet linkSet : parsedPage.getLinkSets()) {
                    linkSets.add(linkSet);
                }
            }

            return linkSets;
        } catch (ParsingException ex) {
            throw new RapiderException(ex.getMessage(), ex);
        } catch (SearchEngineException ex) {
            throw new RapiderException(ex.getMessage(), ex);
        }
    }

    public static void main (String[] arg) throws RapiderException, MalformedURLException, ParsingException {

        SearchResult result = new SearchResult();
//        result.setUrl("http://rifasproject.org/webpages/showcontent/2803");
//        result.setVisibleUrl("http://rifasproject.org/webpages/showcontent/2803");
        result.setUrl("http://www.mortality.sk/goro.htm");
        result.setVisibleUrl("http://www.mortality.sk/goro.htm");

        UrlDownloader downloader = new DefaultUrlDownloader();
        downloader.setUrlTimeout(2000);
        WebPage page = downloader.download(result);
        System.out.println(page.getContent());

        UrlParser parser = new RapidshareUrlParser();
        parser.setComparator(new LinkComparator(RegexRepository.RAPIDSHARE_LINK));
        parser.setLinkRegex(RegexRepository.RAPIDSHARE_LINK);
        parser.setLinkDescRegex(RegexRepository.RAPIDSHARE_LINK_DESC);

        WebPage webPage = parser.parse(page);
        for (LinkSet linkSet : webPage.getLinkSets()) {
            System.out.println(linkSet.getName());
            for (Link link : linkSet.getLinks()) {
                System.out.println(link.getUrl());
            }
        }
        System.exit(0);


        
//
//        SearchEngine engine = new GoogleCacheSearchEngine();
//        engine.setStorage(InternetStorage.RAPIDSHARE);
//        engine.setName("test");
//        engine.setDescription("test");
//        engine.setType(SearchEngineType.REST);
//        engine.setSource(new URL("http://ajax.googleapis.com/ajax/services/search/web?v=1.0"));
//
//        Tag tag = new Tag();
//        tag.setBinder("x files");
//
//        UrlParser parser = new RapidshareUrlParser();
//        parser.setComparator(new LinkComparator(RegexRepository.RAPIDSHARE_LINK));
//        parser.setLinkRegex(RegexRepository.RAPIDSHARE_LINK);
//        parser.setLinkDescRegex(RegexRepository.RAPIDSHARE_LINK_DESC);
//
//        rapider.setEngine(engine);
//        rapider.setDownloader(downloader);
//        rapider.setParser(parser);
//        rapider.run(tag, 1);
    }
}
