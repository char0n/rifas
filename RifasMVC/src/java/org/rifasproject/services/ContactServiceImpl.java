/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.services;

import org.rifasproject.domain.command.ContactForm;
import org.rifasproject.util.mail.MailException;
import org.rifasproject.util.mail.MailSender;

/**
 *
 * @author char0n
 */
public class ContactServiceImpl extends AbstractContactService {

    @Override
    public void sendMail(ContactForm data) throws ServiceException {
        try {
            this.mailSender.send(MailSender.FROM, data.getFrom(), data.getSubject(), data.getMessage());
        } catch (MailException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
    }

}
