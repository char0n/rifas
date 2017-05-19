/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import org.rifasproject.domain.command.ContactForm;
import org.rifasproject.util.mail.SimpleMailSender;

/**
 *
 * @author root
 */
public interface ContactService {
    
    public void setMailSender(SimpleMailSender sender);

    public void sendMail(ContactForm data) throws ServiceException;
}
