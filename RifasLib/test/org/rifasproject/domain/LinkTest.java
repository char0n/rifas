/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.domain;

import org.rifasproject.domain.Link;
import java.net.MalformedURLException;
import java.net.URL;
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
public class LinkTest {

    public LinkTest() {
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
     * Test of isActive method, of class Link.
     */
    @Test
    public void testIsActive() {
        System.out.println("isActive");
        Link instance = new Link();
        boolean expResult = true;
        instance.setActive(expResult);
        boolean result = instance.isActive();
        assertEquals(expResult, result);
    }

    /**
     * Test of setActive method, of class Link.
     */
    @Test
    public void testSetActive() {
        System.out.println("setActive");
        Link instance = new Link();
        boolean expResult = true;
        instance.setActive(expResult);
        boolean result = instance.isActive();
        assertEquals(expResult, result);
    }

    /**
     * Test of getUrl method, of class Link.
     */
    @Test
    public void testGetUrl() throws MalformedURLException {
        System.out.println("getUrl");
        Link instance = new Link();
        String expResult = "http://www.mortality.sk";
        instance.setUrl(expResult);
        String result = instance.getUrl();
        assertEquals(expResult, result);
    }

    /**
     * Test of setUrl method, of class Link.
     */
    @Test
    public void testSetUrl() throws MalformedURLException {
        System.out.println("setUrl");
        Link instance = new Link();
        String expResult = "http://www.mortality.sk";
        instance.setUrl(expResult);
        String result = instance.getUrl();
        assertEquals(expResult, result);
    }

    /**
     * Test of setSize method, of class Link.
     */
    @Test
    public void testSetSize()
    {
        System.out.println("setSize");
        Link instance = new Link();
        int expResult = 25;
        instance.setSize(expResult);
        int result = instance.getSize();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSize method, of class Link.
     */
    @Test
    public void testGetSize()
    {
        System.out.println("getSize");
        Link instance = new Link();
        int expResult = 25;
        instance.setSize(expResult);
        int result = instance.getSize();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Link.
     */
    @Test
    public void testToString() throws MalformedURLException {
        System.out.println("toString");
        Link instance = new Link();
        instance.setUrl("http://www.mortality.sk");
        String expResult = "http://www.mortality.sk";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Link.
     */
    @Test
    public void testEquals() throws MalformedURLException {
        System.out.println("equals");
        Link obj = new Link();
        obj.setUrl("http://www.mortality.sk");
        obj.setActive(true);

        Link instance = new Link();
        instance.setUrl("http://www.mortality.sk");
        instance.setActive(true);

        boolean result = instance.equals(obj);

        assertTrue(result);
    }

    /**
     * Test of hashCode method, of class Link.
     */
    @Test
    public void testHashCode() throws MalformedURLException {
        System.out.println("hashCode");
        Link instance = new Link();
        instance.setActive(true);
        instance.setUrl("http://www.mortality.sk");
        int expResult = 557685516;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }

}