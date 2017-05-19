/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

/**
 *
 * @author char0n
 */
public class BaseModelAndView extends ModelAndView {

    public BaseModelAndView(String viewName) {
        super(viewName);
    }

    public BaseModelAndView() {
    }

    public BaseModelAndView(View view, String modelName, Object modelObject) {
        super(view, modelName, modelObject);
    }

    public BaseModelAndView(String viewName, String modelName, Object modelObject) {
        super(viewName, modelName, modelObject);
    }

    public BaseModelAndView(View view, Map model) {
        super(view, model);
    }

    public BaseModelAndView(String viewName, Map model) {
        super(viewName, model);
    }

    public BaseModelAndView(View view) {
        super(view);
    }


    public void setPageDescription(String pageDescription) {
        this.addObject("pageDescription", pageDescription);
    }

    public void setPageKeywords(String pageKeywords) {
        this.addObject("pageKeywords", pageKeywords);
    }

    public void setPageTitle(String pageTitle) {
        this.addObject("pageTitle", pageTitle);
    }

    public void addStylesheet(String name) {
        if (!this.getModel().containsKey("stylesheets")) {
            this.addObject("stylesheets", new ArrayList<String>());
        }
        List<String> styles = (List<String>) this.getModel().get("stylesheets");
        styles.add(name);
    }

    public void addMetaHeader(String name, String content) {
        if (!this.getModel().containsKey("metaHeaders")) {
            this.addObject("metaHeaders", new HashMap<String, String>());
        }
        Map<String, String> metaHeaders = (Map<String, String>) this.getModel().get("metaHeaders");
        metaHeaders.put(name, content);
    }

    protected void setPaginationProperties(int currentIndex, long itemsCount, int itemsPerPage, String urlPattern) {
           this.addObject("p_currentIndex", currentIndex);
           this.addObject("p_itemsCount", itemsCount);
           this.addObject("p_itemsPerPage", itemsPerPage);
           this.addObject("p_urlPattern", urlPattern);
    }
}
