/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;
import org.rifasproject.services.LinkSetService;
import org.rifasproject.services.ServiceException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author char0n
 */
public class LuceneIndexInitializerContextListener implements ServletContextListener {

    private final Logger log = Logger.getLogger(LuceneIndexInitializerContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ServletContext servletContext = sce.getServletContext();
            ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            LinkSetService linkSetService = (LinkSetService) appContext.getBean("linkSetService");
            linkSetService.initialLinkSetIndex();
        } catch (ServiceException ex) {
            log.warn("Error inicializing application", ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
