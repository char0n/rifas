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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONObject;
import org.openpaste.services.Openpaste;
import org.rifasproject.domain.InternetSearchEngine;
import org.rifasproject.domain.InternetStorage;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.domain.LinkSetType;
import org.rifasproject.domain.Tag;
import org.rifasproject.domain.WebPage;
import org.rifasproject.services.HibernateResult;
import org.rifasproject.services.ImdbService;
import org.rifasproject.services.LinkSetService;
import org.rifasproject.services.ServiceException;
import org.rifasproject.util.LinkSetSortOrder;
import org.rifasproject.util.LinkSetSortOrderUtil;
import org.rifasproject.util.LinkSetTypeUtil;
import org.rifasproject.util.ResultSetFilter;
import org.rifasproject.util.SearchSortUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/linksets/*")
public class LinkSetsController {

    @Autowired private LinkSetService linkSetService;
    @Autowired private ImdbService imdbService;

    private final int maxItemsPerPage = 8;


    @RequestMapping(value="/linksets/check.htm", method=RequestMethod.POST)
    public ModelAndView check(@RequestParam("uuid") String uuid) {
        JSONObject json  = new JSONObject();
        ModelAndView mav = new ModelAndView();
        LinkSet linkSet  = null;

        try {
            linkSet = this.linkSetService.getLinkSetByUuid(uuid);
            if (linkSet == null) {
                throw new ServiceException("LinkSet with UUID "+uuid+" doesn't exist");
            }
        } catch (ServiceException ex) {
            try {
                json.append("status", "KO");
            } catch (JSONException ex1) { }
        }

        try {
            linkSet = this.linkSetService.checkLinkSet(linkSet);
            this.linkSetService.persist(linkSet);
        } catch (ServiceException ex) {
            try {
                json.append("status", "KO");
            } catch (JSONException ex1) { }
        }

        try {
            json.append("status", "OK");
        } catch (JSONException ex) { }

        mav.addObject("json", json.toString());

        return mav;
    }

    @RequestMapping(value="/linksets/show.htm", method=RequestMethod.GET)
    public ModelAndView show(@RequestParam("uuid") String uuid, HttpServletRequest request) throws ServiceException {

        LinkSet linkSet = this.linkSetService.getLinkSetByUuid(uuid);
        String title;
        String desc;
        String keywords;
        BaseModelAndView mav;
        List<LinkSet> related;
        Map<Long, List<Link>> relatedLinks = null;

        Long[] relatedIds;

        // Try to locate LinkSet in archive
        if (linkSet == null) {
            RedirectView redir = new RedirectView(request.getContextPath()+"/archive/show/"+uuid);
            redir.setExposeModelAttributes(false);
            return new ModelAndView(redir);
        }

        mav = new BaseModelAndView();

        String select = request.getParameter("select");
        if (select != null) {
            mav.addObject("select", select);
        }

        title    = (linkSet.getName() != null) ? linkSet.getName() : linkSet.getUuid();
        title    = title+" - "+linkSet.getStorage().toString().substring(0, 1).toUpperCase()+linkSet.getStorage().toString().substring(1).toLowerCase();
        title    = title+" LinkSet";
        desc     = title;
        keywords = (linkSet.getName() != null) ? linkSet.getName().toLowerCase().replaceAll("\\s+", ", ")+", linkset" : "linkset";

        mav.setPageTitle(title);
        mav.setPageDescription(desc);
        mav.setPageKeywords(keywords);
        mav.addObject("linkSet", linkSet);

        // Related LinkSets
        related    = this.linkSetService.getRelatedLinkSets(linkSet, 0, 11);
        if (related.size() > 0) {
            relatedIds = new Long[related.size()];
            int index = 0;
            for (LinkSet l : related) {
                relatedIds[index++] = l.getId();
            }
            relatedLinks = this.linkSetService.getLinksByLinkSetIds(relatedIds);
        }
        mav.addObject("relatedLinkSets", related);
        mav.addObject("relatedLinks"   , relatedLinks);

        // Imdb related titles
        Map<Integer, String> relatedTitles = null;
        try {
            relatedTitles = this.imdbService.getMatchedTitles(linkSet.getName(), 400);
        } catch (ServiceException ex) { }
        mav.addObject("relatedTitles", relatedTitles);

        return mav;
    }

    @RequestMapping(value="/linksets/dis.htm", method=RequestMethod.POST)
    public ModelAndView dis(@RequestParam(value="level", required=true) int level,
                            @RequestParam(value="keyword", required=true) String keyword,
                            @RequestParam(value="search_engine", required=true) String engine,
                            @RequestParam(value="storage", required=true) String storage,
                            HttpServletRequest request) {

        JSONObject json     = new JSONObject();
        ModelAndView mav    = new ModelAndView();

        // Initialize SearchEngine & Storage
        InternetSearchEngine internetSearchEngine;
        try {
            internetSearchEngine = InternetSearchEngine.valueOf(engine.toUpperCase());
        } catch (IllegalArgumentException ex) {
            internetSearchEngine = InternetSearchEngine.GOOGLE;
        }
        InternetStorage internetStorage;
        try {
            internetStorage = InternetStorage.valueOf(storage.toUpperCase());
        } catch (IllegalArgumentException ex) {
            internetStorage = InternetStorage.RAPIDSHARE;
        }


        // Initialize No auto DIS repetition
        HttpSession session = request.getSession();
        HashSet<String> autoDIS;
        if (session.getAttribute("autoDISstorage") == null) {
            autoDIS = new HashSet<String>();
            autoDIS.add(keyword);
            session.setAttribute("autoDISstorage", autoDIS);
        } else {
            autoDIS = (HashSet<String>) session.getAttribute("autoDISstorage");
        }
        autoDIS.add(keyword);

        try {
            for (int i = 1; i <= level; i++) {
                this.linkSetService.searchOnInternetAndPersist(keyword, internetSearchEngine, internetStorage, i);
            }
        } catch (Exception ex) {
            try {
                json.append("status", "KO");
                mav.addObject("json", json.toString(1));
                return mav;
            } catch (JSONException ex1) {  }
        }

        try {
            json.append("status"    , "OK");            
            mav.addObject("json", json.toString(1));
        } catch (JSONException ex) { }
        
        return mav;
    }

    @RequestMapping(value="/linksets/search.htm", method=RequestMethod.POST)
    public ModelAndView search(@RequestParam("keyword") String keyword,
                               @RequestParam("fileType") String fileType,
                               @RequestParam("storage") String storage,
                               @RequestParam("filter") String filter,
                               HttpServletRequest request) throws Exception {

        // Keyword contains empty string; cannot be searched
        if (keyword != null && keyword.trim().equals("")) {
            RedirectView redir = new RedirectView(request.getContextPath()+"/");
            redir.setExposeModelAttributes(false); 

            return new ModelAndView(redir);
        }

        // LinkSetType processing
        LinkSetType linkSetType = LinkSetTypeUtil.searchLinkSetTypeByAcronym(fileType);
        fileType                = LinkSetTypeUtil.searchAcronymByLinkSetType(linkSetType);

        // Storage type processing
        InternetStorage intStorage = null;
        if (storage != null) {
            for (InternetStorage s : InternetStorage.values()) {
                if (s.name().toLowerCase().equals(storage)) {
                    intStorage = s;
                    break;
                }
            }
        }
        intStorage = (intStorage == null) ? InternetStorage.RAPIDSHARE : intStorage;

        // Results filter processing
        ResultSetFilter rFilter = null;
        if (filter != null) {
            for (ResultSetFilter f : ResultSetFilter.values()) {
                if (f.value().equals(filter)) {
                    rFilter  = f;
                    break;
                }
            }
        }
        rFilter = (rFilter == null) ? ResultSetFilter.ALL : rFilter;

        StringBuffer urlBuffer = new StringBuffer(request.getContextPath());
        urlBuffer.append("/linksets/search/");
        try {
            // Replacing "/" for empty string. bad handling. TODO
            urlBuffer.append(URLEncoder.encode(keyword.trim().replace("/", ""), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new Exception("Unsuported encoding while encoding keyword to URI", ex);
        }
        urlBuffer.append("/in/"+fileType);
        urlBuffer.append("/storage/"+intStorage.name().toLowerCase());
        urlBuffer.append("/filter/"+rFilter.value());
        
        RedirectView redir = new RedirectView(urlBuffer.toString());
        redir.setExposeModelAttributes(false);

        return new ModelAndView(redir);
    }

    @RequestMapping(value="/linksets/search.htm", method=RequestMethod.GET)
    public ModelAndView search(HttpServletRequest request) throws ServiceException {

        String keyword   = request.getParameter("keyword");
        String type      = request.getParameter("fileType");
        String storage   = request.getParameter("storage");
        String page      = request.getParameter("page");
        String sortBy    = request.getParameter("sortBy");
        String sortOrder = request.getParameter("sortOrder");
        String filter    = request.getParameter("filter");

        // Keyword contains empty string; cannot be searched
        if (keyword != null && keyword.equals("")) {
            RedirectView redir = new RedirectView(request.getContextPath()+"/");
            redir.setExposeModelAttributes(false);

            return new ModelAndView(redir);
        }

        // LinkSetType processing
        LinkSetType linkSetType    = LinkSetTypeUtil.searchLinkSetTypeByAcronym(type);
        type                       = LinkSetTypeUtil.searchAcronymByLinkSetType(linkSetType);
        // SortBy processing
        int sortById = (sortBy != null && SearchSortUtil.getSorting().containsKey(Integer.parseInt(sortBy))) ? Integer.parseInt(sortBy) : SearchSortUtil.getDefaultSortId();
        // Sort Order
        LinkSetSortOrder order = LinkSetSortOrderUtil.getSortByName(sortOrder);

        // Storage type processing
        InternetStorage intStorage = null;
        if (storage != null) {
            for (InternetStorage s : InternetStorage.values()) {
                if (s.name().toLowerCase().equals(storage)) {
                    intStorage = s;
                    break;
                }
            }
        }
        intStorage = (intStorage == null) ? InternetStorage.RAPIDSHARE : intStorage;

        // Results filter processing
        ResultSetFilter rFilter = null;
        if (filter != null) {
            for (ResultSetFilter f : ResultSetFilter.values()) {
                if (f.value().equals(filter)) {
                    rFilter = f;
                    break;
                }
            }
        }
        rFilter = (rFilter == null) ? ResultSetFilter.ALL : rFilter;


        BaseModelAndView mav      = new BaseModelAndView();
        String linkSetTypeText    = LinkSetTypeUtil.searchTextByAcronym(type);


        StringBuffer url = new StringBuffer("linksets/search/");
        String searchURL = null;
        int intPage = (page == null) ? 1 : Integer.parseInt(page);

        // Don't search; just set search params
        if (keyword != null) {
            keyword = keyword.trim();
            Map<Long, List<Link>> links;
            Map<Long, List<Tag>> tags;
            Map<Long, List<WebPage>> pages;
            Map<Long, Long> commentCounts;
            int resultSize;
            int firstResult  = (intPage - 1) * this.maxItemsPerPage;

            HibernateResult<LinkSet, Integer> result = null;
            result = this.linkSetService.searchInLibrary(keyword, linkSetType, intStorage, rFilter, sortById, order, firstResult, this.maxItemsPerPage);
            // Tell search engines not to index this page
            if (result.getResultSize() == 0 || request.getParameter("sortBy") != null || request.getParameter("sortOrder") != null) {
                mav.addMetaHeader("robots"      , "noindex, follow");
                mav.addMetaHeader("X-Robots-Tag", "noindex, follow");
            }

            mav.addObject("linkSets", result.getResults());

            Long[] linkSetIds = new Long[result.getResults().size()];
            int index = 0;
            for (LinkSet linkSet : result.getResults()) {
                linkSetIds[index++] = linkSet.getId();
            }

            // Load lazy collections only if there are some LinkSets
            if (linkSetIds.length != 0) {
                links         = this.linkSetService.getLinksByLinkSetIds(linkSetIds);
                tags          = this.linkSetService.getTagsByLinkSetIds(linkSetIds);
                pages         = this.linkSetService.getWebPagesByLinkSetIds(linkSetIds);
                commentCounts = this.linkSetService.getCommentCountsByLinkSetIds(linkSetIds);

                mav.addObject("links"        , links);
                mav.addObject("tags"         , tags);
                mav.addObject("pages"        , pages);
                mav.addObject("commentCounts", commentCounts); 
            }


            resultSize = result.getResultSize();

            try {
                url.append(URLEncoder.encode(keyword, "UTF-8"));

                url.append("/in/"+type);
                url.append("/storage/"+intStorage.name().toLowerCase());
                url.append("/filter/"+rFilter.value());
                searchURL = url.toString();
                if (sortById != 1 && order.name().toLowerCase().equals("desc")) {
                    url.append("/sort/"+sortById+"/"+order.name().toLowerCase());
                }

                mav.setPaginationProperties(intPage, resultSize, this.maxItemsPerPage, url.toString());

                String title    = keyword.substring(0, 1).toUpperCase()+keyword.substring(1).toLowerCase()+" "+linkSetTypeText.toLowerCase()+" search - "+intStorage.name();
                String keywords = (!linkSetType.equals(LinkSetType.UNSPECIFIED)) ? keyword.toLowerCase()+", "+"linkset"+", "+linkSetTypeText.toLowerCase()+", "+intStorage.name().toLowerCase() : keyword.toLowerCase()+", "+"linkset"+", "+intStorage.name().toLowerCase();
                mav.setPageTitle(title);
                mav.setPageDescription(title);
                mav.setPageKeywords(keywords);
            } catch (UnsupportedEncodingException ex) {
                throw new ServiceException("Unsuported encoding while encoding keyword to URI", ex);
            }

        } else {
            String title    = (!linkSetType.equals(LinkSetType.UNSPECIFIED)) ? linkSetTypeText+" - "+intStorage.name() : intStorage.name();
            String keywords = (!linkSetType.equals(LinkSetType.UNSPECIFIED)) ? "linkset"+", "+linkSetTypeText.toLowerCase()+", "+intStorage.name().toLowerCase() : "linkset"+", "+intStorage.name().toLowerCase();
            mav.setPageTitle(title);
            mav.setPageDescription(title);
            mav.setPageKeywords(keywords);

            ServletContext sc;
            File movieFile;
            BufferedReader reader = null;

            try {
                sc        = request.getSession().getServletContext();
                movieFile = new File(sc.getRealPath("out.txt"));
                reader    = new BufferedReader(new FileReader(movieFile));
                String movie;
                List<String> movies = new ArrayList<String>();
                int max        = 2840;
                int min        = 0;
                int toProcess  = 100;
                int random     = min + (int)(Math.random() * ((max - min) + 1));
                int startLine  = (random + toProcess > max) ? (max - toProcess) : random;
                int line = 0;
                while ((movie = reader.readLine()) != null) {
                    if (line >= startLine && --toProcess > 0) {
                        movies.add(movie);
                    }
                    ++line;
                }
                mav.addObject("importedMovies", movies);
            } catch (FileNotFoundException ex) {
                throw new ServiceException(ex);
            } catch (IOException ex) {
                throw new ServiceException(ex);
            } finally {
                if (reader != null) try { reader.close(); } catch (IOException ex) { /*ignore*/ }
            }
        }

        // Auto DIS initialization @TODO - no storage filer
        if (request.getSession().getAttribute("autoDISstorage") == null) {
            mav.addObject("autoDIS", true);
        } else {
            HashSet<String> autoDIS = (HashSet<String>) request.getSession().getAttribute("autoDISstorage");
            if (!autoDIS.contains(keyword)) {
                mav.addObject("autoDIS", true);
            } else {
                mav.addObject("autoDIS", false);
            }
        }

        mav.addObject("keyword" , keyword);
        mav.addObject("activeLinkSetType", type);
        mav.addObject("storage", intStorage.name().toLowerCase());
        mav.addObject("filter", rFilter.value());
        mav.addObject("filterAcronym", rFilter.value());
        mav.addObject("sorting", SearchSortUtil.getSorting());
        mav.addObject("sortById", sortById);
        mav.addObject("sortingOrder", order);
        mav.addObject("searchURL"   , searchURL);
        mav.addObject("page", intPage);

        return mav;
    }

    @RequestMapping(value="/linksets/exportOpenpaste.htm", method=RequestMethod.POST)
    public ModelAndView exportOpenpaste(@RequestParam(value="uuid", required=true) String uuid) throws ServiceException {

        Openpaste api = this.linkSetService.exportLinkSet(uuid);

        ModelAndView mav = new ModelAndView();
        mav.addObject("openpasteAPI", api);
        return mav;
    }
}
