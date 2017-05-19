/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.services;

import org.rifasproject.services.MimeHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rifasproject.domain.MimeType;
import static org.junit.Assert.*;

/**
 *
 * @author root
 */
public class MimeHelperTest {

    public MimeHelperTest() {
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
     * Test of get method, of class MimeHelper.
     */
    @Test
    public void testGet() throws Exception {
        System.out.println("get");
        String mimeType = "text/html; charset=UTF-8";
        MimeType expResult = MimeType.HTML;
        MimeType result = MimeHelper.get(mimeType);
        assertEquals(expResult, result);
    }

}