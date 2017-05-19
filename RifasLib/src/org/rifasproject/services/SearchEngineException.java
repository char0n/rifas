/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.services;

/**
 *
 * @author root
 */
public class SearchEngineException extends ServiceException {

    /**
     * Creates a new instance of <code>SearchEngineException</code> without detail message.
     */
    public SearchEngineException() {
    }

    public SearchEngineException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructs an instance of <code>SearchEngineException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SearchEngineException(String msg) {
        super(msg);
    }
}
