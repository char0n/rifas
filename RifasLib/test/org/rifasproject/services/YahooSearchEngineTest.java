/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import java.net.URL;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.rifasproject.domain.InternetSearchEngine;
import org.rifasproject.domain.InternetStorage;
import org.rifasproject.domain.SearchResult;

/**
 *
 * @author root
 */
public class YahooSearchEngineTest {

    public YahooSearchEngineTest() {
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
     * Test of getAssignedEngineType method, of class YahooSearchEngine.
     */
    @Test
    public void testGetAssignedEngineType() {
        System.out.println("getAssignedEngineType");
        YahooSearchEngine instance = new YahooSearchEngine();
        InternetSearchEngine expResult = InternetSearchEngine.YAHOO;
        InternetSearchEngine result = instance.getAssignedEngineType();
        assertEquals(expResult, result);
    }

    /**
     * Test of getResults method, of class YahooSearchEngine.
     */
    @Test
    public void testGetResults_String() throws Exception {
        System.out.println("getResults");
        String query = "i robot";
        YahooSearchEngine instance = new YahooSearchEngine();
        instance.setSource(new URL("http://boss.yahooapis.com/ysearch/web/v1/"));
        instance.setStorage(InternetStorage.RAPIDSHARE);
        List<SearchResult> expResult = instance.getResults(query);
        assertTrue(expResult.size() == 10);
    }

    /**
     * Test of getResults method, of class YahooSearchEngine.
     */
    @Test
    public void testGetResults_String_Integer() throws Exception {
        System.out.println("getResults");
        System.out.println("getResults");
        String query = "i robot";
        YahooSearchEngine instance = new YahooSearchEngine();
        instance.setSource(new URL("http://boss.yahooapis.com/ysearch/web/v1/"));
        instance.setStorage(InternetStorage.RAPIDSHARE);
        List<SearchResult> expResult = instance.getResults(query, 2);
        assertTrue(expResult.size() == 10);
    }

}