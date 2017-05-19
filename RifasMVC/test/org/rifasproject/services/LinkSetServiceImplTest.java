/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rifasproject.domain.InternetStorage;
import org.rifasproject.domain.LinkSetType;
import org.rifasproject.util.LinkSetSortOrder;
import org.rifasproject.util.ResultSetFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author root
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations ={"file:web/WEB-INF/applicationContext.xml"})
public class LinkSetServiceImplTest {

    @Autowired private LinkSetService linkSetService;

    @BeforeClass
    public static void setUpClass() throws Exception {
        DOMConfigurator.configure("test/log4j.xml");
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

    @Test
    public void testSearchInLibrary() throws Exception {
        System.out.println("searchInLibrary");
        this.linkSetService.searchInLibrary("test", LinkSetType.UNSPECIFIED, InternetStorage.RAPIDSHARE, ResultSetFilter.ALL, 1, LinkSetSortOrder.ASC, 0, 10);
    }
}
