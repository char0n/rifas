/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc.interceptor;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import org.rifasproject.domain.DictionaryItem;
import org.rifasproject.domain.InternetSearchEngine;
import org.rifasproject.domain.LinkSetType;
import org.rifasproject.domain.LinkSetTypeSearchBarItem;
import org.rifasproject.domain.MenuItem;
import org.rifasproject.util.DictionarySet;
import org.rifasproject.util.LinkSetTypeUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author char0n
 */
public class CommonDataInterceptor extends HandlerInterceptorAdapter {

    private static final List<MenuItem> menu = new ArrayList<MenuItem>();
    static {
        DictionarySet menuItemProperties = new DictionarySet();
        menuItemProperties.add(new DictionaryItem("acronym", ""));
        menuItemProperties.add(new DictionaryItem("text", "Home"));
        menuItemProperties.add(new DictionaryItem("url", ""));
        MenuItem homeMenuItem = new MenuItem();
        homeMenuItem.setDictionary(menuItemProperties);

        menuItemProperties = new DictionarySet();
        menuItemProperties.add(new DictionaryItem("acronym", "downloads"));
        menuItemProperties.add(new DictionaryItem("text", "Download"));
        menuItemProperties.add(new DictionaryItem("url", "downloads/"));
        MenuItem downloadMenuItem = new MenuItem();
        downloadMenuItem.setDictionary(menuItemProperties);

        menuItemProperties = new DictionarySet();
        menuItemProperties.add(new DictionaryItem("acronym", "about"));
        menuItemProperties.add(new DictionaryItem("text", "About"));
        menuItemProperties.add(new DictionaryItem("url", "about/"));
        MenuItem aboutMenuItem = new MenuItem();
        aboutMenuItem.setDictionary(menuItemProperties);

        menuItemProperties = new DictionarySet();
        menuItemProperties.add(new DictionaryItem("acronym", "services"));
        menuItemProperties.add(new DictionaryItem("text"   , "Services"));
        menuItemProperties.add(new DictionaryItem("url"    , "services/"));
        MenuItem servicesMenuItem = new MenuItem();
        servicesMenuItem.setDictionary(menuItemProperties);

        menuItemProperties = new DictionarySet();
        menuItemProperties.add(new DictionaryItem("acronym", "contact"));
        menuItemProperties.add(new DictionaryItem("text", "Contact"));
        menuItemProperties.add(new DictionaryItem("url", "contact/"));
        MenuItem contactMenuItem = new MenuItem();
        contactMenuItem.setDictionary(menuItemProperties);


        menu.add(homeMenuItem);
        menu.add(downloadMenuItem);
        menu.add(aboutMenuItem);
        menu.add(servicesMenuItem);
        menu.add(contactMenuItem);
    }

    @Override
    public void postHandle(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, java.lang.Object handler, ModelAndView modelAndView)
    {
        this.addMainMenu(modelAndView);
        this.addFileTypes(modelAndView);
        this.addDefaultPageProperties(modelAndView);
        this.addAddDISSearchEngine(request, modelAndView);
    }

    protected void addDefaultPageProperties(ModelAndView target) {

        // No model awaillable
        if (target == null) return;

        int intPage     = (target.getModel().containsKey("p_currentIndex")) ? Integer.parseInt(target.getModel().get("p_currentIndex").toString()) : 1;
        String page     = (intPage == 1) ? "" : " - Page #"+intPage;
        String title    = "RIFAS Project - Rapid Internet File Allocation Service"+page;
        String desc     = title+", searching for files on various download engines";
        String keywords = "rapid, internet, file, allocation, search, service, rapidshare";

        if (!target.getModel().containsKey("pageTitle")) {
            target.addObject("pageTitle", title);
        } else {
            target.addObject("pageTitle", target.getModel().get("pageTitle")+" - "+title);
        }
        if (!target.getModel().containsKey("pageDescription")) {
            target.addObject("pageDescription", desc);
        } else {
            target.addObject("pageDescription", target.getModel().get("pageDescription")+" - "+desc);
        }
        if (!target.getModel().containsKey("pageKeywords")) {
            target.addObject("pageKeywords", keywords);
        } else {
            target.addObject("pageKeywords", target.getModel().get("pageKeywords")+", "+keywords);
        }
    }

    protected void addMainMenu(ModelAndView target)
    {
        // No model awaillable
        if (target == null) return;

        target.addObject("menuItems", menu);
    }

    protected void addFileTypes(ModelAndView target)
    {
        // No model awaillable
        if (target == null) return;

        List<LinkSetTypeSearchBarItem> searchBarItems = LinkSetTypeUtil.getSearchBarItems();
        boolean hasActiveLinkSetType = target.getModel().containsKey("activeLinkSetType");
        if (hasActiveLinkSetType == false)
        {
            target.addObject("activeLinkSetType", LinkSetTypeUtil.searchAcronymByLinkSetType(LinkSetType.UNSPECIFIED));
        }

        target.addObject("linkSetTypes", searchBarItems);
    }

    protected void addAddDISSearchEngine(javax.servlet.http.HttpServletRequest request, ModelAndView target) {

        // No model awaillable
        if (target == null) return;

        Cookie[] cookies = request.getCookies();
        String defaultSearchEngine = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("DISSearchEngineCookie")) {
                    defaultSearchEngine = cookie.getValue();
                    break;
                }
            }
        }


        try {
            defaultSearchEngine = InternetSearchEngine.valueOf(defaultSearchEngine.toUpperCase()).name().toLowerCase();
            target.addObject("DISSearchEngine", defaultSearchEngine);
        } catch (IllegalArgumentException ex) {
            target.addObject("DISSearchEngine", InternetSearchEngine.GOOGLE.name().toLowerCase());
        } catch (NullPointerException ex) {
            target.addObject("DISSearchEngine", InternetSearchEngine.GOOGLE.name().toLowerCase());
        }
    }
}
