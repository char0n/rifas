/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.rifasproject.domain.InternetStorage;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.domain.Movie;
import org.rifasproject.services.ImdbService;
import org.rifasproject.services.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import sk.mortality.util.opensubtitles.OpensubtitlesXmlRpcServiceImpl;

/**
 *
 * @author char0n
 */
@Controller
@RequestMapping("/imdb/*")
public class ImdbController {

    @Autowired private ImdbService imdbService;

    @RequestMapping(value="/imdb/cover.htm", method=RequestMethod.GET)
    public void cover(@RequestParam(value="imdbId", required=true) int imdbId, HttpServletResponse response) throws IOException {
        
        byte[] imageData = null;

        try {
            Movie m   = this.imdbService.getTitleByImdbId(imdbId);
            imageData = m.getCoverData();
        } catch (ServiceException ex) { }

        if (imageData != null) {
            response.setContentType("image/jpeg");
            ServletOutputStream responseOutputStream = response.getOutputStream();
            responseOutputStream.write(imageData);
            responseOutputStream.flush();
            responseOutputStream.close();
        } else {
            response.sendError(response.SC_NOT_FOUND);
        }
    }

    @RequestMapping(value="/imdb/search.htm", method=RequestMethod.POST)
    public ModelAndView search(@RequestParam(value="title", required=true) String title, HttpServletRequest request) {
        try {
            title = URLEncoder.encode(title, "UTF-8").trim();
        } catch (UnsupportedEncodingException ex) {
            title = "";
        }
        RedirectView redir = new RedirectView(request.getContextPath()+"/imdb/search/"+title);
        redir.setExposeModelAttributes(false);
        return new ModelAndView(redir);
    }

    @RequestMapping(value="/imdb/search.htm", method=RequestMethod.GET)
    public ModelAndView search(HttpServletRequest request) {
        BaseModelAndView mav         = new BaseModelAndView();
        String title                 = request.getParameter("title");
        String pageTitle             = "Imdb Search";
        String pageKeywords          = "imdb, title, subtitle";
        RedirectView redirectView;

        mav.addObject("activeMenuItem", "services");

        // Show default page
        if (title == null) {
            mav.setPageTitle(pageTitle);
            mav.setPageDescription(pageTitle);
            mav.setPageKeywords(pageKeywords);
            return mav;
        }

        // Search phrase is empty
        if (title.trim().equals("")) {
            // Redirect back to search form
            redirectView = new RedirectView(request.getContextPath()+"/imdb/search/");
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }

        // Searching for imdb matches
        Object[] result;
        Map<Integer, String> matches;
        Movie firstMatch = null;
        try {
            result     = this.imdbService.searchImdbTitles(title);
            matches    = (Map<Integer, String>) result[0];
            firstMatch = (Movie) result[1];
        } catch (ServiceException ex) {
            // Redirect back to search form
            redirectView = new RedirectView(request.getContextPath()+"/imdb/search/");
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }

        // Only one match found; redirect straight to imdb title page
        if (matches.size() == 1) {
            try {
                redirectView = new RedirectView(request.getContextPath() + "/imdb/title/" + firstMatch.getId() + "/" + URLEncoder.encode(firstMatch.getTitle(), "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                redirectView = new RedirectView(request.getContextPath() + "/imdb/title/" + firstMatch.getId() + "/" + firstMatch.getTitle());
            }
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }

        // No matches found, tell search engines no to index
        if (matches.size() == 0) {
            mav.addMetaHeader("robots"      , "noindex, follow");
            mav.addMetaHeader("X-Robots-Tag", "noindex, follow");        
        }

        // Page Properties setup
        pageTitle     = title.substring(0, 1).toUpperCase()+title.substring(1).toLowerCase()+" - "+pageTitle;
        pageKeywords += ", "+title.toLowerCase();
        mav.setPageTitle(pageTitle);
        mav.setPageDescription(pageTitle);
        mav.setPageKeywords(pageKeywords);

        mav.addObject("title"     , title);
        mav.addObject("matches"   , matches);
        mav.addObject("firstMatch", firstMatch);

        return mav;
    }

    @RequestMapping(value="/imdb/title.htm", method=RequestMethod.GET)
    public ModelAndView title(@RequestParam(value="imdbId", required=true) int imdbId, HttpServletRequest request)  {
        
        BaseModelAndView mav = new BaseModelAndView();
        InternetStorage defaultStorage = InternetStorage.RAPIDSHARE;
        Locale subtitleLanguage = request.getLocale();
        Object[] result;
        Movie movie;
        List<LinkSet> relatedLinkSets;
        Map<Long, List<Link>> relatedLinks;
        Set<OpensubtitlesXmlRpcServiceImpl.Subtitle> subtitles;

        // Loading imdb title related data
        try {
            result          = this.imdbService.getImdbTitleData(imdbId, defaultStorage, subtitleLanguage);
            movie           = (Movie) result[0];
            relatedLinkSets = (List<LinkSet>) result[1];
            relatedLinks    = (Map<Long, List<Link>>) result[2];
            subtitles       = (Set<OpensubtitlesXmlRpcServiceImpl.Subtitle>) result[3];
        } catch (ServiceException ex) {
            // Redirect back to search form
            RedirectView r1 = new RedirectView(request.getContextPath()+"/imdb/search/");
            r1.setExposeModelAttributes(false);
            return new ModelAndView(r1);
        }

        mav.addObject("movie", movie);
        mav.addObject("relatedLinkSets", relatedLinkSets);
        mav.addObject("relatedLinks"   , relatedLinks);
        mav.addObject("defaultStorage" , defaultStorage);
        mav.addObject("storages"       , InternetStorage.values());
        mav.addObject("subtitles"      , subtitles);
        mav.addObject("movieTitle"     , movie.getTitle());
        mav.addObject("imdbId"         , imdbId);

        // Page properties setup
        mav.setPageTitle(movie.getTitle()+" ("+movie.getYear()+") - Imdb title");
        mav.setPageDescription(movie.getTitle()+" ("+movie.getYear()+") - Imdb title");
        mav.setPageKeywords("imdb, title, subtitle, movie, release, "+movie.getTitle().toLowerCase());
        mav.addObject("activeMenuItem", "services");

        
        // Subtitles lazy load flag
        if (subtitles == null) {
            mav.addObject("subtitlesLazyLoad", true);
        } else {
            mav.addObject("subtitlesLazyLoad", false);
        }
        
        return mav;
    }

    @RequestMapping(value="/imdb/searchAsyncSubtitles.htm", method=RequestMethod.POST)
    public ModelAndView searchAsyncSubtitles(@RequestParam(value="imdbId", required=true) int imdbId, HttpServletRequest request) throws ServiceException {
        ModelAndView mav        = new ModelAndView();
        Locale subtitleLanguage = request.getLocale();
        Set<OpensubtitlesXmlRpcServiceImpl.Subtitle> subtitles = null;
        Movie movie;

        try {
            subtitles = this.imdbService.getAsyncSubtitles(imdbId, subtitleLanguage);
            movie     = this.imdbService.getTitleByImdbId(imdbId);
            mav.addObject("movieTitle", movie.getTitle());
            mav.addObject("imdbId"    , imdbId);
        } catch (ServiceException ex) {
            mav.addObject("errorLazyLoadSubtitles", true);
        }
        mav.addObject("subtitles", subtitles);

        return mav;
    }
}