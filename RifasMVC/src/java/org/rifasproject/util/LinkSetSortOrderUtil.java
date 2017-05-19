/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util;

/**
 *
 * @author char0n
 */
public class LinkSetSortOrderUtil {

    public static LinkSetSortOrder getSortByName(String name) {
        if (name == null )
            return LinkSetSortOrder.DESC;
        else if (name.equals(LinkSetSortOrder.DESC.name().toLowerCase()))
            return LinkSetSortOrder.DESC;
        else
            return LinkSetSortOrder.ASC;
    }
}
