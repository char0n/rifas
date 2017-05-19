/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import org.rifasproject.util.mail.MailSender;
import org.rifasproject.util.mail.SimpleMailSender;

/**
 *
 * @author char0n
 */
abstract public class AbstractContactService implements ContactService {

    protected MailSender mailSender;

    @Override
    public void setMailSender(SimpleMailSender sender) {
        this.mailSender = sender;
    }
}
