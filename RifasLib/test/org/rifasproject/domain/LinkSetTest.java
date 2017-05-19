/*
 * GPLv2 with Classpath Exception
 */

// TODO: implement new domain methods to test
package org.rifasproject.domain;

import java.util.HashSet;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.LinkSet;
import java.util.Set;
import java.util.TreeSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rifasproject.domain.LinkSetType;
import org.rifasproject.domain.Tag;
import org.rifasproject.domain.WebPage;
import static org.junit.Assert.*;

/**
 *
 * @author root
 */
public class LinkSetTest {

    public LinkSetTest() {
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
     * Test of getLinks method, of class LinkSet.
     */
    @Test
    public void testGetLinks() {
        System.out.println("getLinks");
        LinkSet instance = new LinkSet();
        Set<Link> expResult = new TreeSet<Link>();
        instance.setLinks(expResult);
        Set<Link> result = instance.getLinks();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLinks method, of class LinkSet.
     */
    @Test
    public void testSetLinks() {
        System.out.println("setLinks");
        LinkSet instance = new LinkSet();
        Set<Link> expResult = new TreeSet<Link>();
        instance.setLinks(expResult);
        Set<Link> result = instance.getLinks();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMovie method, of class LinkSet.
     */
    @Test
    public void testGetTags() {
        System.out.println("getTags");
        LinkSet instance = new LinkSet();
        Set<Tag> expResult = new HashSet<Tag>();
        expResult.add(new Tag());
        instance.setTags(expResult);
        Set<Tag> result = instance.getTags();
        assertEquals(expResult, result);
    }

    /**
     * Test of setMovie method, of class LinkSet.
     */
    @Test
    public void testSetTags() {
        System.out.println("setTags");
        LinkSet instance = new LinkSet();
        Set<Tag> expResult = new HashSet<Tag>();
        expResult.add(new Tag());
        instance.setTags(expResult);
        Set<Tag> result = instance.getTags();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPages method, of class LinkSet.
     */
    @Test
    public void testsetPages()
    {
        System.out.println("setPage");
        LinkSet instance = new LinkSet();
        Set<WebPage> expResult = new HashSet<WebPage>();
        expResult.add(new WebPage());
        instance.setPages(expResult);
        Set<WebPage> result = instance.getPages();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPages method, of class LinkSet.
     */
    @Test
    public void testgetPages()
    {
        System.out.println("getPage");
        LinkSet instance = new LinkSet();
        Set<WebPage> expResult = new HashSet<WebPage>();
        expResult.add(new WebPage());
        instance.setPages(expResult);
        Set<WebPage> result = instance.getPages();
        assertEquals(expResult, result);
    }

    /**
     * Test of setType, of class LinkSet.
     */
    @Test
    public void testsetType()
    {
        System.out.println("setType");
        LinkSet instance = new LinkSet();
        LinkSetType result = LinkSetType.AUDIO;
        instance.setType(result);
        LinkSetType expResult = instance.getType();
        assertEquals(expResult, result);
    }

    /**
     * Test of getType, of class LinkSet.
     */
    @Test
    public void testgetType()
    {
        System.out.println("getType");
        LinkSet instance = new LinkSet();
        LinkSetType result = LinkSetType.AUDIO;
        instance.setType(result);
        LinkSetType expResult = instance.getType();
        assertEquals(expResult, result);
    }
}