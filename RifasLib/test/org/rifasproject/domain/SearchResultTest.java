/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.domain;

import org.rifasproject.domain.SearchResult;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rifasproject.domain.Tag;
import static org.junit.Assert.*;

/**
 *
 * @author root
 */
public class SearchResultTest {

    public SearchResultTest() {
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
     * Test of getUrl method, of class SearchResult.
     */
    @Test
    public void testGetUrl() throws MalformedURLException {
        System.out.println("getUrl");
        SearchResult instance = new SearchResult();
        String expResult = "http://www.mortality.sk";
        instance.setUrl(expResult);
        String result = instance.getUrl();
        assertEquals(expResult, result);
    }

    /**
     * Test of setUrl method, of class SearchResult.
     */
    @Test
    public void testSetUrl() throws MalformedURLException {
        System.out.println("setUrl");
        SearchResult instance = new SearchResult();
        String expResult = "http://www.mortality.sk";
        instance.setUrl(expResult);
        String result = instance.getUrl();
        assertEquals(expResult, result);
    }

    /**
     * Test of getVisibleUrl method, of class SearchResult.
     */
    @Test
    public void testGetVisibleUrl() throws MalformedURLException {
        System.out.println("getVisibleUrl");
        SearchResult instance = new SearchResult();
        String expResult = "http://www.mortality.sk";
        instance.setVisibleUrl(expResult);
        String result = instance.getVisibleUrl();
        assertEquals(expResult, result);
    }

    /**
     * Test of setVisibleUrl method, of class SearchResult.
     */
    @Test
    public void testSetVisibleUrl() throws MalformedURLException {
        System.out.println("setVisibleUrl");
        SearchResult instance = new SearchResult();
        String expResult = "http://www.mortality.sk";
        instance.setVisibleUrl(expResult);
        String result = instance.getVisibleUrl();
        assertEquals(expResult, result);
    }

    /**
     * Test of getContent method, of class SearchResult.
     */
    @Test
    public void testGetContent() {
        System.out.println("getContent");
        SearchResult instance = new SearchResult();
        String expResult = "test";
        instance.setContent(expResult);
        String result = instance.getContent();
        assertEquals(expResult, result);
    }

    /**
     * Test of setContent method, of class SearchResult.
     */
    @Test
    public void testSetContent() {
        System.out.println("setContent");
        SearchResult instance = new SearchResult();
        String expResult = "test";
        instance.setContent(expResult);
        String result = instance.getContent();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTitle method, of class SearchResult.
     */
    @Test
    public void testGetTitle() {
        System.out.println("getTitle");
        SearchResult instance = new SearchResult();
        String expResult = "test";
        instance.setTitle(expResult);
        String result = instance.getTitle();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTitle method, of class SearchResult.
     */
    @Test
    public void testSetTitle() {
        System.out.println("setTitle");
        SearchResult instance = new SearchResult();
        String expResult = "test";
        instance.setTitle(expResult);
        String result = instance.getTitle();
        assertEquals(expResult, result);
    }

    /**
     * Test of getQuery method, of class SearchResult.
     */
    @Test
    public void testGetQuery() {
        System.out.println("getQuery");
        SearchResult instance = new SearchResult();
        String expResult = "test";
        instance.setQuery(expResult);
        String result = instance.getQuery();
        assertEquals(expResult, result);
    }

    /**
     * Test of setQuery method, of class SearchResult.
     */
    @Test
    public void testSetTag() {
        System.out.println("setQuery");
        SearchResult instance = new SearchResult();
        String expResult = "test";
        instance.setQuery(expResult);
        String result = instance.getQuery();
        assertEquals(expResult, result);
    }
}