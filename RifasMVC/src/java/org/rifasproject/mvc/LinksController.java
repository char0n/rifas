/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author char0n
 */
@Controller
@RequestMapping("/links/*")
public class LinksController {

    @RequestMapping(value="/links/list.htm", method=RequestMethod.GET)
    public ModelAndView list() {
        BaseModelAndView mav = new BaseModelAndView();
        mav.setPageTitle("Links - Link exchange");
        mav.setPageDescription("Links - Link exchange");
        mav.setPageKeywords("link, links, link exchange, link directory");

        return mav;
    }

}
