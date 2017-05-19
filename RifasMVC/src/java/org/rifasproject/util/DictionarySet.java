/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.rifasproject.domain.DictionaryItem;

/**
 *
 * @author char0n
 */
public class DictionarySet extends HashSet<DictionaryItem> {

    private final Map<String, DictionaryItem> cache = new HashMap<String, DictionaryItem>();

    public DictionaryItem findByKey(String key) {
        // Quick load from cache
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        // Slow loop search
        DictionaryItem toReturn = null;
        for (DictionaryItem i : this) {
            if (i.getKey().equals(key)) {
                toReturn = i;
                cache.put(key, i);
                break;
            }
        }
        return toReturn;
    }
}
