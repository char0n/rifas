/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util;

/**
 *
 * @author char0n
 */
public class LinkSizeUtil {

    // Setup some common file size measurements.
    private static double KB = 1024L;
    private static double MB = 1024L * KB;
    private static double GB = 1024L * MB;
    private static double TB = 1024L * GB;


    public static String formatSize(double size) {

        // Finding out the appropriate range
        if (size < KB) {
	        return size+"B";
        } else if (size < MB) {
	        return (Math.round((size * 100d) / KB)/100d)+"kB";
        } else if (size < GB) {
	        return (Math.round((size * 100d) / MB)/100d)+"MB";
        } else if (size < TB) {
	        return (Math.round((size * 100d) / GB)/100d)+"GB";
        } else {
	        return (Math.round((size * 100d) / TB)/100d)+"TB";
        }
    }
}
