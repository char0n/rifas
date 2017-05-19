/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.gui.util;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import org.rifasproject.util.LinkSizeUtil;

/**
 *
 * @author char0n
 */
public class SizeTableCellRenderer extends DefaultTableCellRenderer
{
    @Override
    public void setValue(Object value)
    {
        double size   =  Double.valueOf(value.toString());

        this.setText(LinkSizeUtil.formatSize(size));
        this.setHorizontalAlignment(SwingConstants.RIGHT);
    }
}