/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.gui.listener;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.rifasproject.domain.Link;
import org.rifasproject.domain.LinkSet;
import org.rifasproject.gui.RifasGUIApp;
import org.rifasproject.gui.RifasGUIView;

/**
 *
 * @author char0n
 */
public class LinksetTreeExpansionListener implements TreeExpansionListener {

    private JTable linksTable;

    public LinksetTreeExpansionListener(JTable linksTable)
    {
        this.linksTable = linksTable;
    }

    public void treeExpanded(TreeExpansionEvent event) {
        TreePath path   = event.getPath();
        LinkSet linkSet = (LinkSet) ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();

        DefaultTableModel model  = (DefaultTableModel) this.linksTable.getModel();
        model.setRowCount(0);

        for (Link link : linkSet.getLinks())
        {
            model.addRow(new Object[] { link, link.getSize() });
        }

        RifasGUIApp app          = (RifasGUIApp) RifasGUIApp.getInstance();
        RifasGUIView mainFrame  = (RifasGUIView) app.getMainView();
        mainFrame.setExportEnabled(true);
    }

    public void treeCollapsed(TreeExpansionEvent event) {
        DefaultTableModel model = (DefaultTableModel) this.linksTable.getModel();
        model.setRowCount(0);

        RifasGUIApp app          = (RifasGUIApp) RifasGUIApp.getInstance();
        RifasGUIView mainFrame  = (RifasGUIView) app.getMainView();
        mainFrame.setExportEnabled(false);
    }

}
