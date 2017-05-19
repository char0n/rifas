/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util;

import org.rifasproject.domain.InternetStorage;
import org.rifasproject.domain.Link;

/**
 *
 * @author char0n
 */
public class LinkObfuscatorUtil {

    public static LinkObfuscator getObfuscator(InternetStorage storage) {

        LinkObfuscator obfuscator = null;

        switch (storage) {
            case RAPIDSHARE:
                obfuscator = new LinkObfuscatorUtil.RapidshareLinkObfuscator();
        }

        return obfuscator;
    }

    public static String encodeNCR(String url) {
        StringBuffer nsr = new StringBuffer();
        for (char c : url.toCharArray()) {
            nsr.append("&#"+(int)c+";");
        }
        return nsr.toString();
    }

    static class RapidshareLinkObfuscator implements LinkObfuscator {

        @Override
        public String obfuscate(Link link) {

            return link.getUrl().replaceFirst("http://rapidshare.com/files/[0-9]+/", "http://rapidshare.com/files/.../");
        }
    }
}
