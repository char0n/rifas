/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.domain;

import org.rifasproject.domain.SearchResult;
import org.rifasproject.domain.MimeType;
import org.rifasproject.domain.WebPage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author root
 */
public class WebPageTest {

    public WebPageTest() {
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
     * Test of getContent method, of class WebPage.
     */
    @Test
    public void testGetContent() {
        System.out.println("getContent");
        WebPage instance = new WebPage();
        String expResult = "test";
        instance.setContent(expResult);
        String result = instance.getContent();
        assertEquals(expResult, result);
    }

    /**
     * Test of setContent method, of class WebPage.
     */
    @Test
    public void testSetContent() {
        System.out.println("setContent");
        WebPage instance = new WebPage();
        String expResult = "test";
        instance.setContent(expResult);
        String result = instance.getContent();
        assertEquals(expResult, result);
    }

    /**
     * Test of getType method, of class WebPage.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        MimeType expType = MimeType.HTML;
        WebPage instance = new WebPage();
        instance.setType(expType);
        MimeType result = instance.getType();
        assertEquals(expType, result);
    }

    /**
     * Test of setType method, of class WebPage.
     */
    @Test
    public void testSetType() {
        System.out.println("setType");
        MimeType expType = MimeType.HTML;
        WebPage instance = new WebPage();
        instance.setType(expType);
        MimeType result = instance.getType();
        assertEquals(expType, result);
    }

    /**
     * Test of getResult method, of class WebPage.
     */
    @Test
    public void testGetResult() {
        System.out.println("getResult");
        SearchResult expResult = new SearchResult();
        WebPage instance = new WebPage();
        instance.setResult(expResult);
        SearchResult result = instance.getResult();
        assertEquals(expResult, result);
    }

    /**
     * Test of setResult method, of class WebPage.
     */
    @Test
    public void testSetResult() {
        System.out.println("setResult");
        SearchResult expResult = new SearchResult();
        WebPage instance = new WebPage();
        instance.setResult(expResult);
        SearchResult result = instance.getResult();
        assertEquals(expResult, result);
    }

}