/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.search.bridge;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

/**
 *
 * @author char0n
 */
public class WebPageUrlFieldBridge implements FieldBridge {

    @Override
    public void set(String name, Object value, Document document, LuceneOptions options) {
        String url;
        try {
            URL url1 = new URL(String.valueOf(value));
            url      = url1.getFile();
        } catch (MalformedURLException ex) {
            url      = String.valueOf(value);
        }

        url = url.replace(".", " ").replace("_", " ").replace("-", " ").replace("/", " ").replace(":", " ").replace(",", " ").replace("=", "");

        Field field = new Field(name, url, options.getStore(), options.getIndex(), options.getTermVector());
        field.setBoost(options.getBoost());
        document.add(field);
    }
}