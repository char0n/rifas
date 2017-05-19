/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.search.bridge;

import java.util.Set;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.rifasproject.domain.Link;

/**
 *
 * @author char0n
 */
public class LinkSetLinksFieldBridge implements FieldBridge {

    @Override
    public void set(String name, Object value, Document document, LuceneOptions options) {

        Set<Link> links = (Set<Link>) value;
        int size      = 0;
        int sizeBytes = 0;
        for (Link link : links) {
            size++;
            sizeBytes += link.getSize();
        }

        Field linkSetSizeField = new Field("linkSetSize", String.valueOf(size), options.getStore(), options.getIndex(), options.getTermVector());
        linkSetSizeField.setBoost(options.getBoost());
        document.add(linkSetSizeField);

        Field linkSetSizeBytesField = new Field("linkSetSizeBytes", String.valueOf(sizeBytes), options.getStore(), options.getIndex(), options.getTermVector());
        linkSetSizeBytesField.setBoost(options.getBoost());
        document.add(linkSetSizeBytesField);
    }

}
