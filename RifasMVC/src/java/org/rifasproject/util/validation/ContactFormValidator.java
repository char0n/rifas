/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util.validation;

import org.rifasproject.domain.command.ContactForm;
import sk.mortality.util.validation.BrokenRule;
import sk.mortality.util.validation.ValidationResult;
import sk.mortality.util.validation.Validator;
import sk.mortality.util.validation.rule.EmailValidationRule;

/**
 *
 * @author char0n
 */
public class ContactFormValidator implements Validator<ContactForm> {

    @Override
    public boolean isValidForSave(ContactForm cf) {
        return this.validateForSave(cf).isValid();
    }

    @Override
    public boolean isValidForUpdate(ContactForm arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValidationResult validateForSave(ContactForm cf) {
        ValidationResult valResult = new ValidationResult();

        if (cf.getFrom() == null) {
            valResult.add(new BrokenRule("from", "You must fill-in your email address"));
        }
        if (cf.getFrom() != null && cf.getFrom().trim().equals("")) {
            valResult.add(new BrokenRule("from", "You must fill-in your email address"));
        }
        if (cf.getFrom() != null) {
            EmailValidationRule evr = new EmailValidationRule("email", cf.getFrom());
            if (evr.validate().isValid() == false) {
                valResult.add(new BrokenRule("from", "Your email address in not valid email address", cf.getFrom()));
            }
        }
        if (cf.getSubject() == null) {
            valResult.add(new BrokenRule("subject", "Please fill-in subject of your message"));
        }
        if (cf.getSubject() != null && cf.getSubject().trim().equals("")) {
            valResult.add(new BrokenRule("subject", "Please fill-in subject of your message"));
        }
        if (cf.getMessage() == null) {
            valResult.add(new BrokenRule("message", "Please fill-in email message"));
        }
        if (cf.getMessage() != null && cf.getMessage().trim().equals("")) {
            valResult.add(new BrokenRule("message", "Please fill-in email message"));
        }

        return valResult;
    }

    @Override
    public ValidationResult validateForUpdate(ContactForm arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
