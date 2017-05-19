/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util;

import java.util.Comparator;

/**
 *
 * @author char0n
 */
public class StringLengthComparator implements Comparator<String> {

    private static final int EQUAL  = 0;
    private static final int BEFORE = -1;
    private static final int AFTER  = 1;
    
 
    @Override
    public int compare(String o1, String o2) {
        if (o1.length() < o2.length()) return BEFORE;
        if (o1.length() > o2.length()) return AFTER;

        return EQUAL;
    }

}
