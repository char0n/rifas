/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.domain;

import java.io.Serializable;

/**
 *
 * @author root
 */
public enum InternetStorage implements Serializable {

    RAPIDSHARE("http://rapidshare.com");

    private String url;

    InternetStorage(String url) {
        this.url = url;
    }

    public String getUrl()
    {
        return this.url;
    }
}
