/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.services;

/**
 *
 * @author root
 */
public class LinkCheckerException extends ServiceException {

    public LinkCheckerException(String msg) {
        super(msg);
    }

    public LinkCheckerException(String message, Throwable cause) {
        super(message, cause);
    }

    public LinkCheckerException() {
    }
}
