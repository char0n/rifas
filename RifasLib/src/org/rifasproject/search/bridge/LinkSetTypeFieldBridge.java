/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.search.bridge;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.rifasproject.domain.LinkSetType;

/**
 *
 * @author char0n
 */
public class LinkSetTypeFieldBridge implements FieldBridge {

    @Override
    public void set(String name, Object value, Document document, LuceneOptions options) {

        LinkSetType type = (LinkSetType) value;

        Field linkSetSizeField = new Field("type", String.valueOf(type.ordinal()), options.getStore(), options.getIndex(), options.getTermVector());
        linkSetSizeField.setBoost(options.getBoost());
        document.add(linkSetSizeField);             
    }
}
