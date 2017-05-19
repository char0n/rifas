/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util.mail;

import java.util.Date;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 *
 * @author char0n
 */
public class SimpleMailSender extends MailSenderImpl {

    @Override
    public void send(String to, String from, String subject, String message) throws MailException {
        MimeMessage msg;
        MimeMessageHelper helper;
        try {
            msg    = this.mailSender.createMimeMessage();
            helper = new MimeMessageHelper(msg, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setReplyTo(from);
            helper.setSentDate(new Date());
            msg.setText(message);
            this.mailSender.send(msg);
        } catch (MessagingException ex) {
            throw new MailException("Error sending email", ex);
        }
    }

}
