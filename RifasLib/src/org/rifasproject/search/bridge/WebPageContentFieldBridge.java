/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.search.bridge;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

/**
 *
 * @author char0n
 */
public class WebPageContentFieldBridge implements FieldBridge {

    private static Pattern pattern = Pattern.compile("(?i)(<title.*?>)(.+?)(</title>)");

    @Override
    public void set(String name, Object value, Document document, LuceneOptions options) {

        String content = String.valueOf(value);
        String title;
        Matcher m = pattern.matcher(content);
        if (m.find()) {
            title = m.group(2).trim();
        } else {
            title = "";
        }

        Field field = new Field("pages.title", title, options.getStore(), options.getIndex(), options.getTermVector());
        field.setBoost(options.getBoost());
        document.add(field);
    }
}
