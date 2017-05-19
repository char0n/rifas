/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.domain.WebPage;
import org.rifasproject.services.HibernateResult;
import org.rifasproject.services.WebPageService;
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
@RequestMapping("/webpages/*")
public class WebPagesController {

    private WebPageService webPageService;
    private final int MAX_LINKSETS = 15;

    @Autowired
    public void setWebPageService(WebPageService webPageService) {
        this.webPageService = webPageService;
    }

    @RequestMapping(value="/webpages/show.htm", method=RequestMethod.GET)
    public ModelAndView show(@RequestParam("id") Long id, HttpServletRequest request) throws Exception {

        String strPage   = request.getParameter("page");
        int intPage      = (strPage == null) ? 1 : Integer.parseInt(strPage);
        int firstResult  = (intPage - 1) * MAX_LINKSETS;

        WebPage page = this.webPageService.getWebPageById(id);
        HibernateResult<LinkSet, Long> result   = this.webPageService.getLinkSetsByWebPageId(page.getId(), firstResult, MAX_LINKSETS);
        List<LinkSet> linkSets       = result.getResults();
        Long linkSetSize             = result.getResultSize();
        Map<Long, List<Link>> links = null;
        if (linkSets.size() != 0) {
            Long[] linkSetsIds = new Long[linkSets.size()];
            int index = 0;
            for (LinkSet linkSet : linkSets) linkSetsIds[index++] = linkSet.getId();

            links = this.webPageService.getLinksByLinkSetIds(linkSetsIds);
        }


        BaseModelAndView mav = new BaseModelAndView();
        mav.setPaginationProperties(intPage, linkSetSize, MAX_LINKSETS, new StringBuffer().append("webpages/show/").append(id).toString());
        String title = "WebPage source #"+page.getId()+((intPage > 1) ? " - Page #"+intPage : "");
        mav.setPageTitle(title);
        mav.setPageDescription(title);
        mav.setPageKeywords("page, source");
        mav.addObject("page", page);
        mav.addObject("linkSets", linkSets);
        mav.addObject("linkSetSize", linkSetSize);
        mav.addObject("links", links);
        mav.addObject("pProps", this.webPageService.getWebPageProperties(page));
        // Tell search engine not to index this page
        mav.addMetaHeader("robots"      , "noindex, follow");
        mav.addMetaHeader("X-Robots-Tag", "noindex, follow");
        
        return mav;
    }

    @RequestMapping(value="/webpages/showContent.htm", method=RequestMethod.GET)
    public ModelAndView showContent(@RequestParam("id") Long id) throws Exception {

        WebPage page           = this.webPageService.getWebPageById(id);

        ModelAndView mav = new ModelAndView();
        mav.addObject("pageContent", page.getContent());

        return mav;
    }
}
