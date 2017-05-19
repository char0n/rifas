/*
 * GPLv2 with Classpath Exception
 */

package org.rifasproject.services;

/**
 *
 * @author char0n
 */
public class ParsingException extends ServiceException {

    public ParsingException(String msg) {
        super(msg);
    }

    public ParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParsingException() {
    }

}
