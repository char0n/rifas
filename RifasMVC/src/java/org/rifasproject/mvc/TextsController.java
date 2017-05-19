/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author char0n
 */
@Controller
@RequestMapping("/texts/*")
public class TextsController {

    @RequestMapping(value="/texts/about.htm", method=RequestMethod.GET)
    public ModelAndView info() {

        BaseModelAndView mav = new BaseModelAndView();
        mav.setPageTitle("About RIFAS");
        mav.setPageDescription("About RIFAS");
        mav.setPageKeywords("about");
        mav.addObject("activeMenuItem", "about");

        return mav;
    }

    @RequestMapping(value="/texts/searchtips.htm", method=RequestMethod.GET)
    public ModelAndView searchtips() {

        BaseModelAndView mav = new BaseModelAndView();
        mav.setPageTitle("Search Tips");
        mav.setPageDescription("Search Tips");
        mav.setPageKeywords("search, tips, tip");

        return mav;
    }

    @RequestMapping(value="/texts/privacyPolicy.htm", method=RequestMethod.GET)
    public ModelAndView privacyPolicy() {
        BaseModelAndView mav = new BaseModelAndView();
        mav.setPageTitle("Privacy Policy");
        mav.setPageDescription("Privacy Policy");
        mav.setPageKeywords("privacy, policy, privacy policy");

        return mav;
    }

    @RequestMapping(value="/texts/termsOfUse.htm", method=RequestMethod.GET)
    public ModelAndView termsOfUse() {
        BaseModelAndView mav = new BaseModelAndView();
        mav.setPageTitle("Terms of Use");
        mav.setPageDescription("Terms of Use");
        mav.setPageKeywords("terms, use, terms of use");

        return mav;
    }

    @RequestMapping(value="/texts/disclaimer.htm", method=RequestMethod.GET)
    public ModelAndView disclaimer() {
        BaseModelAndView mav = new BaseModelAndView();
        mav.setPageTitle("Disclaimer");
        mav.setPageDescription("Disclaimer");
        mav.setPageKeywords("disclaimer");

        return mav;
    }

    @RequestMapping(value="/texts/bookmarks.htm", method=RequestMethod.POST)
    public ModelAndView bookmars() {
        ModelAndView mav = new ModelAndView();
        return mav;
    }

    @RequestMapping(value="/texts/filter.htm", method=RequestMethod.POST)
    public ModelAndView filter() {
        ModelAndView mav = new ModelAndView();
        return mav;
    }

    @RequestMapping(value="/texts/storage.htm", method=RequestMethod.POST)
    public ModelAndView storage() {
        ModelAndView mav = new ModelAndView();
        return mav;
    }

    @RequestMapping(value="/texts/searchEngines.htm", method=RequestMethod.POST)
    public ModelAndView searchEngines() {
        ModelAndView mav = new ModelAndView();
        return mav;
    }

    @RequestMapping(value="/texts/setSearchEngine.htm", method=RequestMethod.POST)
    public ModelAndView setDISSearchEngine(@RequestParam(value="searchEngine", required=true) String searchEngine,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {
        Cookie cookie = new Cookie("DISSearchEngineCookie", searchEngine);
        cookie.setPath(request.getContextPath()+"/");
        response.addCookie(cookie);

        RedirectView redir = new RedirectView(request.getHeader("Referer"));
        redir.setExposeModelAttributes(false); 
        return new ModelAndView(redir);
    }

    @RequestMapping(value="/texts/linkSetExport.htm", method=RequestMethod.POST)
    public ModelAndView linkSetExport(@RequestParam(value="uuid", required=true) String uuid) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("linkSetUUID", uuid);
        return mav;
    }

    @RequestMapping(value="/texts/services.htm", method=RequestMethod.GET)
    public ModelAndView services() {
        BaseModelAndView mav = new BaseModelAndView();
        mav.setPageTitle("Services");
        mav.setPageDescription("RIFAS Services");
        mav.setPageKeywords("service, services");
        mav.addObject("activeMenuItem", "services");

        return mav;
    }
}
