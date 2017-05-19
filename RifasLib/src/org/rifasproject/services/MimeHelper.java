/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.rifasproject.domain.MimeType;

/**
 *
 * @author char0n
 */
public class MimeHelper {

    public static MimeType get(String mimeType) throws NoSuchFieldException
    {
        Pattern p = Pattern.compile("^text/html.*");
        Matcher m = p.matcher("");

        MimeType[] types = MimeType.values();
        for (MimeType type : types)
        {
            m.reset(type.getType());
            if (m.find())
            {
                return type;
            }
        }
        throw new NoSuchFieldException("Mime type '"+mimeType+"' is not supported");
    }
}