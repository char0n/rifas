/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.domain;

import java.io.Serializable;
import org.rifasproject.util.DictionarySet;

/**
 *
 * @author char0n
 */
public class MenuItem implements Serializable {

    private DictionarySet dictionary = new DictionarySet();

    public DictionarySet getDictionary() {
        return dictionary;
    }

    public void setDictionary(DictionarySet dictionary) {
        this.dictionary = dictionary;
    }
}
