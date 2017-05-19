/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rifasproject.domain.Movie;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author root
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations ={"file:web/WEB-INF/applicationContext.xml"})
public class ImdbServiceImplTest {

    @Autowired private ImdbService imdbService;

    public ImdbServiceImplTest() {
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
     * Test of getMatchedTitles method, of class ImdbServiceImpl.
     */
    /*
    @Test
    public void testGetMatchedTitles() throws Exception {
        System.out.println("getMatchedTitles");
        String imdbTitle = "I, Robot";
        Map<Integer, String> result = this.imdbService.getMatchedTitles(imdbTitle);
        assertTrue(result.size() > 0);
    }
    */

    @Test
    public void testGetTitlebyImdbId() throws Exception {
        System.out.println("getTitleByImdbId");
        int imdbId = 110413;
        Movie expResult = this.imdbService.getTitleByImdbId(imdbId);
        assertEquals(expResult.getCoverData().length, 7259);
    }
}