/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

/**
 *
 * @author char0n
 */
abstract public class UrlDownloaderImpl implements UrlDownloader  {

    private int urlTimeout;

    @Override
    public void setUrlTimeout(int timeout) {
        this.urlTimeout = timeout;
    }

    @Override
    public int getUrlTimeout() {
        return this.urlTimeout;
    }
}
