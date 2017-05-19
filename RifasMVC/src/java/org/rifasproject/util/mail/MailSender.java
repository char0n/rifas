/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util.mail;

/**
 *
 * @author root
 */
public interface MailSender {
    
    public static final String FROM  = "admin@rifasproject.org";

    public void send(String to, String from, String subject, String message) throws MailException;
}
