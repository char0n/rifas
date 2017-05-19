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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author char0n
 */
@Controller
@RequestMapping("/sitemaps/*")
public class SitemapsController  {

    @RequestMapping(value="/sitemaps/readindex.htm", method=RequestMethod.GET)
    public ModelAndView readindex(HttpServletRequest request) {
        ModelAndView mav  = new ModelAndView();
        ServletContext sc = request.getSession().getServletContext();

        File sitemapIndex     = new File(sc.getRealPath("sitemap/sitemapIndex.xml"));
        BufferedReader reader = null;
        String line;
        StringBuffer xml = new StringBuffer();

        try {
            reader = new BufferedReader(new FileReader(sitemapIndex));
            while ((line = reader.readLine()) != null) {
                xml.append(line+"\n");
            }

        } catch (FileNotFoundException ex) {
            
        } catch (IOException ex) {

        } finally {
            if (reader != null) try {
                reader.close();
            } catch (IOException ex) { }
        }

        mav.addObject("indexData", xml.toString());

        return mav;
    }
}
