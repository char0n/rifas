/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.services;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.domain.SearchResult;
import org.rifasproject.domain.WebPage;
import org.rifasproject.util.LinkComparator;
import org.rifasproject.util.RegexRepository;
import static org.junit.Assert.*;

/**
 *
 * @author root
 */
public class RapidshareUrlParserTest {

    public RapidshareUrlParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of parse method, of class RapidshareUrlParser.
     */
    @Test
    public void testParse() throws Exception {
        System.out.println("parse");
        UrlParser  parser        = new RapidshareUrlParser();
        parser.setLinkRegex(RegexRepository.RAPIDSHARE_LINK);
        parser.setLinkDescRegex(RegexRepository.RAPIDSHARE_LINK_DESC);
        parser.setComparator(new LinkComparator(RegexRepository.RAPIDSHARE_LINK));
        UrlDownloader downloader = new DefaultUrlDownloader();

        SearchResult result = new SearchResult();
        result.setContent("content");
        result.setTitle("title");
        result.setUrl("http://rifasproject.org/webpages/showcontent/20162");
        result.setVisibleUrl("http://rifasproject.org/webpages/showcontent/20162");

        WebPage page       = downloader.download(result);
        parser.parse(page);
        for (LinkSet l : page.getLinkSets()) {
            for (Link link : l.getLinks()) {
                System.out.println(link.getUrl());
            }
            System.out.println();
        }
        assertEquals(page.getLinkSets().size(), 20);
    }

}