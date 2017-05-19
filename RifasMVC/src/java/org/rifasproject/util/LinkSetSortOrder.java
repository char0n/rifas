/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util;

/**
 *
 * @author char0n
 */
public enum LinkSetSortOrder {
    ASC(false),
    DESC(true);

    private boolean order;

    LinkSetSortOrder(boolean order) {
        this.order = order;
    }

    public boolean value() {
        return this.order;
    }
}