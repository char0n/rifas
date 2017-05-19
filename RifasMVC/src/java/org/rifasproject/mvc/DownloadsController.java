/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author char0n
 */
@Controller
@RequestMapping("/downloads/*")
public class DownloadsController {

    @RequestMapping(value="/downloads/list.htm", method=RequestMethod.GET)
    public ModelAndView list() {

        BaseModelAndView mav = new BaseModelAndView();
        mav.setPageTitle("RIFAS Downloads");
        mav.setPageDescription("RIFAS Downloads");
        mav.setPageKeywords("download, downloads");
        mav.addObject("activeMenuItem", "downloads");

        return mav;
    }

    @RequestMapping(value="/downloads/affiliateDownload.htm", method=RequestMethod.GET)
    public ModelAndView affiliateDownload(@RequestParam(value="linkSet", required=true) String linkSetName) {
        BaseModelAndView mav = new BaseModelAndView();
        mav.setPageTitle("Please register to download "+linkSetName+" - special offer - 14 day free trial");
        mav.setPageDescription("Please register to download "+linkSetName+" - special offer - 14 day free trial");
        mav.setPageKeywords("download, "+linkSetName);
        mav.addObject("linkSetName", linkSetName);
        return mav;
    }
}
