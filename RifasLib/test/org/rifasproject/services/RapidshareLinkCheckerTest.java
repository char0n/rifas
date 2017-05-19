/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.services;

import java.util.Set;
import java.util.TreeSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.LinkSet;
import static org.junit.Assert.*;

/**
 *
 * @author root
 */
public class RapidshareLinkCheckerTest {

    public RapidshareLinkCheckerTest() {
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
     * Test of check method, of class RapidshareLinkChecker.
     */
    @Test
    public void testCheck_Link() throws Exception {
        System.out.println("check");
        Link link = new Link();
        link.setActive(false);
        link.setUrl("http://rapidshare.com/files/111655086/The_X_Files_-_1x20_-_Darkness_Falls.part1.rar");
        LinkChecker instance = new RapidshareLinkChecker();
        link = instance.check(link);
        boolean expResult = true;
        boolean result    = link.isActive();
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class RapidshareLinkChecker.
     */
    @Test
    public void testCheck_LinkSet() throws Exception {
        System.out.println("check");
        LinkSet linkSet = new LinkSet();
        Set<Link> links = new TreeSet();
        Link link       = new Link();
        link.setUrl("http://rapidshare.com/files/111655086/The_X_Files_-_1x20_-_Darkness_Falls.part1.rar");
        link.setActive(false);
        links.add(link);
        linkSet.setLinks(links);

        LinkChecker instance = new RapidshareLinkChecker();
        linkSet = instance.check(linkSet);
        link = (Link) linkSet.getLinks().toArray()[0];
        boolean expResult = true;
        boolean result    = link.isActive();
        assertEquals(expResult, result);
    }

}