/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util;

/**
 *
 * @author root
 */
public enum RegexRepository {

    RAPIDSHARE_LINK("(rapidshare\\.com/files/[\\d]+/([a-zA-Z0-9\\.\\_\\-]+))"),
    RAPIDSHARE_LINK_DESC("((\\.|\\-|_)?part[0-9]+)?\\.?(rar|avi|zip|html)?([0-9]{1,3}|\\.html)?$");

    private String regex;

    RegexRepository(String regex) {
        this.regex = regex;
    }

    public String regex()
    {
        return this.regex;
    }
}
