/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.services;

/**
 *
 * @author root
 */
public class ServiceException extends Exception {

    /**
     * Creates a new instance of <code>ServiceException</code> without detail message.
     */
    public ServiceException() {
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }


    /**
     * Constructs an instance of <code>ServiceException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }    
}
