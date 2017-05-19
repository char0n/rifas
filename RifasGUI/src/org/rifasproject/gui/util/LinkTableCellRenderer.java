/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rifasproject.gui.util;

import javax.swing.Icon;
import javax.swing.table.DefaultTableCellRenderer;
import org.jdesktop.application.ResourceMap;
import org.rifasproject.domain.Link;
import org.rifasproject.gui.RifasGUIApp;

/**
 *
 * @author char0n
 */
public class LinkTableCellRenderer extends DefaultTableCellRenderer  {

    @Override
    public void setValue(Object value)
    {
        Link link = (Link) value;
        this.setText(" "+link.getUrl());

        String resource = (link.isActive()) ? "active.icon" : "inactive.icon";
        ResourceMap resourceMap  = RifasGUIApp.getInstance().getContext().getResourceMap(this.getClass());
        Icon icon = resourceMap.getIcon(resource);
        this.setIcon(icon);
    }
}