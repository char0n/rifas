/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.search.bridge;

import java.util.Date;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

/**
 *
 * @author char0n
 */
public class LinkSetLastCheckedFieldBridge implements FieldBridge {

    @Override
    public void set(String name, Object value, Document document, LuceneOptions options) {



        String dateStr = "";
        Date lastChecked = (Date) value;
        if (value != null) {
            dateStr = DateTools.dateToString(lastChecked, Resolution.MINUTE);
        }
        
        String isLastChecked = (value == null) ? "0" : "1";


        Field lastCheckedField = new Field(name, dateStr, options.getStore(), options.getIndex(), options.getTermVector());
        lastCheckedField.setBoost(options.getBoost());
        document.add(lastCheckedField);

        Field isCheckedField = new Field("isChecked", isLastChecked, options.getStore(), options.getIndex(), options.getTermVector());
        isCheckedField.setBoost(options.getBoost());
        document.add(isCheckedField);
    }
}
