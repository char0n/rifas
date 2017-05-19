/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util;

/**
 *
 * @author char0n
 */
public class SearchResultsHighlightTool {

    public static String highlight(String inputText, String keyword) {
        keyword = keyword.trim().replaceAll("\\s+", " ");
        String[] keywords = keyword.split(" ");

        try {
            for (String k : keywords) {
                inputText = inputText.replaceAll("\\b(?i)" + k +"\\b", "<strong>" + "$0" +  "</strong>");
            }
        } catch (Exception ex) {
            return inputText;
        }

        return inputText;
    }
}
