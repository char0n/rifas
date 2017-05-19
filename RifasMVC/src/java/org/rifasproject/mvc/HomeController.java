/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.rifasproject.domain.Comment;
import org.rifasproject.domain.LibraryStatistics;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.domain.Movie;
import org.rifasproject.domain.SearchKeyword;
import org.rifasproject.services.ImdbService;
import org.rifasproject.services.LinkSetService;
import org.rifasproject.services.MaintenanceService;
import org.rifasproject.services.ServiceException;
import org.rifasproject.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author char0n
 */
@Controller
@RequestMapping("/home/*")
public class HomeController {

    @Autowired private StatsService statsService;
    @Autowired private LinkSetService linkSetService;
    @Autowired private MaintenanceService maintenanceService;
    @Autowired private ImdbService imdbService;

    @RequestMapping(value="/home/homepage.htm", method=RequestMethod.GET)
    public ModelAndView showHomePage(HttpServletRequest request) throws ServiceException
    {
        BaseModelAndView mav = new BaseModelAndView();
        mav.addStylesheet("styles/rrc.css");
        mav.addObject("activeMenuItem", "");

        List<SearchKeyword> searchCloud    = this.statsService.getSearchCloud(30);
        List<SearchKeyword> latestSearches = this.statsService.getLatestSearches(30);
        List<Comment> latestComments       = this.statsService.getLatestComments(0, 6);
        Set<Movie> latestImdbTitles        = this.imdbService.getLatestCoveredTitles(3);
        mav.addObject("searchCloud"     , searchCloud);
        mav.addObject("latestSearches"  , latestSearches);
        mav.addObject("latestComments"  , latestComments);
        mav.addObject("latestImdbTitles", latestImdbTitles);

        Map<Long, LinkSet> commentsLinkSets = new HashMap<Long, LinkSet>();
        LinkSet l;
        for (Comment c : latestComments) {
            l = this.linkSetService.loadLinkSet(c.getItemId());
            commentsLinkSets.put(c.getId(), l);
        }
        mav.addObject("commentsLinkSets", commentsLinkSets);

        LibraryStatistics ls = this.statsService.getLibraryStatistics();
        mav.addObject("libraryStats", ls);

        return mav;
    }
}
