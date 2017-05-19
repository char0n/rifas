/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author char0n
 */
public class LibraryStatistics implements Serializable {

    private Map<String, Long> properties = new HashMap<String, Long>();

    public Map<String, Long> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Long> properties) {
        this.properties = properties;
    }

    public Long getProperty(String name) {
        return this.properties.get(name);
    }

    public void addProperty(String name, Long value) {
        this.properties.put(name, value);
    }
}
