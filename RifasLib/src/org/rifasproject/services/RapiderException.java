/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.services;

/**
 *
 * @author root
 */
public class RapiderException extends Exception {

    /**
     * Creates a new instance of <code>RapiderException</code> without detail message.
     */
    public RapiderException() {
    }


    /**
     * Constructs an instance of <code>RapiderException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public RapiderException(String msg) {
        super(msg);
    }

    public RapiderException(String message, Throwable cause) {
        super(message, cause);
    }
}
