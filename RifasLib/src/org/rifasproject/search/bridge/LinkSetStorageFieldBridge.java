/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.search.bridge;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.rifasproject.domain.InternetStorage;

/**
 *
 * @author char0n
 */
public class LinkSetStorageFieldBridge implements FieldBridge {

    @Override
    public void set(String name, Object value, Document document, LuceneOptions options) {

        InternetStorage storage = (InternetStorage) value;

        Field linkSetSizeField = new Field("storage", String.valueOf(storage.ordinal()), options.getStore(), options.getIndex(), options.getTermVector());
        linkSetSizeField.setBoost(options.getBoost());
        document.add(linkSetSizeField);
    }

}
