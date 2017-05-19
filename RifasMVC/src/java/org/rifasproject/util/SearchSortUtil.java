/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util;

import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.search.SortField;

/**
 *
 * @author char0n
 */
public class SearchSortUtil {
    private static Map<Integer, String> sorting = new HashMap<Integer, String>();
    private static Map<Integer, String> sortingFields = new HashMap<Integer, String>();
    private static Map<Integer, Integer> sortingTypes = new HashMap<Integer, Integer>();

    static {
        sorting.put(1, "Relevancy");
        sorting.put(2, "Date");
        sorting.put(3, "CheckDate");
        sorting.put(4, "LinkSetSize");
        sorting.put(5, "Size");

        sortingFields.put(1, null);
        sortingFields.put(2, "created");
        sortingFields.put(3, "lastChecked");
        sortingFields.put(4, "linkSetSize");
        sortingFields.put(5, "linkSetSizeBytes");

        sortingTypes.put(1, null);
        sortingTypes.put(2, SortField.STRING);
        sortingTypes.put(3, SortField.STRING);
        sortingTypes.put(4, SortField.INT);
        sortingTypes.put(5, SortField.INT);
    }

    public static Map<Integer, String> getSorting() {
        return sorting;
    }

    public static int getDefaultSortId() {
        return 1;
    }

    public static String getAcronymById(int id) {
        return sorting.get(id);
    }

    public static int getIdbyAcronym(String acronym) {
        for (int id : sorting.keySet()) {
            if (sorting.get(id).equals(acronym)) {
                return id;
            }
        }

        return -1;
    }

    public static String getFieldNameById(int id) {
        return sortingFields.get(id);
    }

    public static int getSortTypeById(int id) {
        return sortingTypes.get(id);
    }
}
