/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author char0n
 */
public class MD5 {
    public static String encode(String input) throws NoSuchAlgorithmException
    {
        String hash = null;

        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(input.getBytes(), 0, input.length());
        hash = new BigInteger(1, m.digest()).toString(16);

        return hash;
    }
}
