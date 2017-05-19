/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util;

/**
 *
 * @author root
 */
public enum ResultSetFilter {

    ALL("all"),
    CHECKED("checked"),
    UNCHECKED("unchecked"),
    ACTIVE("active"),
    DELETED("deleted");

    private String filter;

    ResultSetFilter(String filter) {
        this.filter = filter;
    }

    public String value() {
        return this.filter;
    }
}
