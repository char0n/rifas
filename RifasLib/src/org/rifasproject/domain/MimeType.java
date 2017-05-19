/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.domain;

import java.io.Serializable;

/**
 *
 * @author root
 */
public enum MimeType implements Serializable {
    HTML("text/html"), XHTML("application/xhtml+xml");

    private String mimeType;

    MimeType(String type)
    {
        this.mimeType = type;
    }

    public String getType()
    {
        return this.mimeType;
    }
}
