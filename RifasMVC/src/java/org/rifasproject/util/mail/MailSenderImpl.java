/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util.mail;

import org.springframework.mail.javamail.JavaMailSender;

/**
 *
 * @author char0n
 */
abstract public class MailSenderImpl implements MailSender {

    protected JavaMailSender mailSender;

    public void setMailSender(JavaMailSender sender) {
        this.mailSender = sender;
    }
}
