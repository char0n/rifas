/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.services;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rifasproject.domain.SearchResult;
import org.rifasproject.domain.WebPage;
import static org.junit.Assert.*;

/**
 *
 * @author root
 */
public class DefaultUrlDownloaderTest {

    public DefaultUrlDownloaderTest() {
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
     * Test of download method, of class DefaultUrlDownloader.
     */
    @Test
    public void testDownload() {
        System.out.println("download");
        SearchResult result = new SearchResult();
        result.setUrl("http://cleveradvice.com");
        UrlDownloader instance = new DefaultUrlDownloader();
        instance.setUrlTimeout(10000);
        WebPage page = instance.download(result);
        System.out.println(page.getContent());
        assertTrue(page.getContent().length() > 0);
    }

}