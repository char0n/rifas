/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.rifasproject.services.ServiceException;
import org.rifasproject.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author char0n
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations ={"file:web/WEB-INF/applicationContext.xml"})
public class HomeControllerIntegrationTest {
    
    @Autowired private StatsService statService;

    @Test
    public void testShowHomePage() {
        try {
            this.statService.getLibraryStatistics();
            this.statService.getLibraryStatistics();
            assertEquals(true, true);
        } catch (ServiceException ex) {
            fail(ex.getMessage());
        }
    }
}
