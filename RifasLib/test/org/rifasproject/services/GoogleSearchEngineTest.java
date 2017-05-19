/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.services;

import java.net.URL;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rifasproject.domain.InternetSearchEngine;
import org.rifasproject.domain.InternetStorage;
import org.rifasproject.domain.Movie;
import org.rifasproject.domain.SearchResult;
import static org.junit.Assert.*;

/**
 *
 * @author root
 */
public class GoogleSearchEngineTest {

    public GoogleSearchEngineTest() {
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
     * Test of getResults method, of class GoogleSearchEngine.
     */
    @Test
    public void testGetResults_Movie() throws Exception {
        System.out.println("getResults");

        SearchEngine engine  = new GoogleSearchEngine();
        engine.setSource(new URL("http://ajax.googleapis.com/ajax/services/search/web?v=1.0"));
        engine.setStorage(InternetStorage.RAPIDSHARE);
        String query = "I, Robot 2004";
        List<SearchResult> results = engine.getResults(query);
        
        assertTrue(results.size() != 0);
    }

    /**
     * Test of getResults method, of class GoogleSearchEngine.
     */
    @Test
    public void testGetResults_Movie_Short() throws Exception {
        System.out.println("getResults");

        SearchEngine engine  = new GoogleSearchEngine();
        engine.setSource(new URL("http://ajax.googleapis.com/ajax/services/search/web?v=1.0"));
        engine.setStorage(InternetStorage.RAPIDSHARE);
        String query = "I, Robot 2004";
        List<SearchResult> results = engine.getResults(query, 1);

        assertTrue(results.size() != 0);
    }

    /**
     * Test of getAssignedEngineType, of class GoogleSearchEngine.
     */
    @Test
    public void testGetAssignedEngineType() throws Exception {
        System.out.println("getAssignedEngineType");

        SearchEngine engine = new GoogleSearchEngine();
        InternetSearchEngine expResult = InternetSearchEngine.GOOGLE;
        assertTrue(expResult.equals(engine.getAssignedEngineType()));
    }
}