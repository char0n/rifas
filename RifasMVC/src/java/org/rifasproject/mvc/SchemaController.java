/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author char0n
 */
@Controller
@RequestMapping("/schema/*")
public class SchemaController {

    @RequestMapping(value="/schema/export.htm", method=RequestMethod.GET)
    public ModelAndView export(HttpServletRequest request)
    {
        ServletContext servletContext = request.getSession().getServletContext();
        ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        LocalSessionFactoryBean beanFactory = (LocalSessionFactoryBean) appContext.getBean("&sessionFactory");
        Configuration cfg = beanFactory.getConfiguration();
        SchemaExport export = new SchemaExport(cfg);
        export.setDelimiter(";");
        export.setOutputFile("schema.ddl");
        export.execute(false, false, false, false);

        File schema = null;
        BufferedReader in = null;
        StringBuffer ddl  = new StringBuffer();

        try {
            schema = new File("schema.ddl");
            in = new BufferedReader(new FileReader(schema));
            String line;
            while ((line = in.readLine()) != null) {
                ddl.append(line+"\n");
            }
        } catch (FileNotFoundException ex) {
            
        } catch (IOException ex) {

        } finally {
            try {
                if (in != null) in.close();
                //if (in != null) schema.delete();
            } catch (IOException ex) { }
        }
        BaseModelAndView mav = new BaseModelAndView();
        mav.addObject("schemaDDL", ddl.toString());
        return mav;
    }
}
