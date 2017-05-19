/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.gui.listener;

import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.rifasproject.domain.Link;


/**
 *
 * @author char0n
 */
public class TableRowDubleClickListener extends MouseAdapter {

   @Override
   public void mouseClicked(MouseEvent e) {
        
       JTable table = (JTable) e.getSource();

       // Two click required
       if (e.getClickCount() == 2)
       {
           // get the index of the row the user has clicked on
           int rowIndex =  table.rowAtPoint(e.getPoint());

           Link link = (Link) ((DefaultTableModel) table.getModel()).getValueAt(rowIndex, 0);
           if (Desktop.isDesktopSupported( ) ) {
                try {
                    Desktop.getDesktop().browse(new URI(link.getLinkSet().getPages().iterator().next().getResult().getUrl()));
                } catch (Exception ex) { }
           }
        }
    }
}
