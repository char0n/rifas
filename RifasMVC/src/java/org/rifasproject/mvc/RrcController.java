/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.domain.LinkSetSource;
import org.rifasproject.domain.WebPage;
import org.rifasproject.services.LinkSetService;
import org.rifasproject.services.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/rrc/*")
public class RrcController {

    private LinkSetService linkSetService;
    private static long DAY = (60L * 60L * 24L * 1000L);

    @Autowired
    public void setLinkSetService(LinkSetService service) {
        this.linkSetService = service;
    }

    @RequestMapping(value="/rrc/demo.htm", method=RequestMethod.GET)
    public ModelAndView demo() {
        BaseModelAndView mav = new BaseModelAndView();
        mav.setPageTitle("Rifas Remote Checker - Demo");
        mav.setPageDescription("Rifas Remote Checker - Demo");
        mav.setPageKeywords("remote, checker, remote checker, demo");

        return mav;
    }

    @RequestMapping(value="/rrc/docs.htm", method=RequestMethod.GET)
    public ModelAndView docs() {
        BaseModelAndView mav = new BaseModelAndView();
        mav.setPageTitle("Rifas Remote Checker - Documentation");
        mav.setPageDescription("Rifas Remote Checker - Documentation");
        mav.setPageKeywords("remote, checker, remote checker, documentation");
        return mav;
    }

    @RequestMapping(value="/rrc/modalWindow.htm", method=RequestMethod.POST)
    public ModelAndView modalWindow() {
        ModelAndView mav = new ModelAndView();        
        return mav;
    }

    @RequestMapping(value="/rrc/check.htm", method=RequestMethod.POST)
    public ModelAndView check(@RequestParam(value="content", required=true) String content,
                              @RequestParam(value="referer", required=true) String referer) throws ServiceException {

        ModelAndView mav = new ModelAndView();

        List<LinkSet> transientLinkSets  = this.linkSetService.getLinkSetsFromStringData(content);
        boolean saveLinkSet = false;
        LinkSet linkSet;
        WebPage page;
        List<Link> links = new ArrayList<Link>();
        for (LinkSet transientLinkSet : transientLinkSets) {
            transientLinkSet.setSource(LinkSetSource.REMOTE_CHECKER);
            transientLinkSet.setCreated(new Date());
            transientLinkSet.setUuid(this.linkSetService.generateLinkSetUuid(transientLinkSet));

            // Loading && Saving unregistered LinkSet
            saveLinkSet = !this.linkSetService.isRegisteredLinkSet(transientLinkSet.getUuid());
            if (saveLinkSet == true) {
                this.linkSetService.checkLinkSet(transientLinkSet);
                this.linkSetService.persistLinkSetTree(transientLinkSet);
                linkSet = transientLinkSet;                
            } else {
                linkSet = this.linkSetService.getLinkSetByUuid(transientLinkSet.getUuid());
                // LinkSet is archived and registered
                if (linkSet == null) {
                    linkSet = transientLinkSet;
                    this.linkSetService.checkLinkSet(linkSet);                    
                }
                // LinkSet is registered in library
                else {
                    Date lastChecked = linkSet.getLastChecked();
                    if (lastChecked == null) {
                        this.linkSetService.checkLinkSet(linkSet);
                        this.linkSetService.persist(linkSet);                        
                    } else {
                        if ((new Date()).getTime() - lastChecked.getTime() > DAY) {
                            this.linkSetService.checkLinkSet(linkSet);
                            this.linkSetService.persist(linkSet);                            
                        }                        
                    }

                    // Adding new WebPage as LinkSet source
                    page = new WebPage();
                    page.setUrl(referer);
                    this.linkSetService.addNewWebPageToLinkSet(page, linkSet);
                }
            }

            links.addAll(linkSet.getLinks());
        }

        mav.addObject("links", links);
        return mav;
    }
}
