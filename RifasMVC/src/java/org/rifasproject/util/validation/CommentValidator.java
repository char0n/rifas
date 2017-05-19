/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util.validation;

import org.rifasproject.domain.Comment;
import sk.mortality.util.validation.AbstractValidator;
import sk.mortality.util.validation.BrokenRule;
import sk.mortality.util.validation.ValidationResult;
import sk.mortality.util.validation.rule.EmailValidationRule;
import sk.mortality.util.validation.rule.UrlValidationRule;

/**
 *
 * @author char0n
 */
public class CommentValidator extends AbstractValidator<Comment> {

    @Override
    public boolean isValidForSave(Comment c) {
        return this.validateForSave(c).isValid();
    }

    @Override
    public boolean isValidForUpdate(Comment arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValidationResult validateForSave(Comment c) {
        ValidationResult valResult = new ValidationResult();

        if (c.getAuthor() == null || c.getAuthor().trim().equals("")) {
            valResult.add(new BrokenRule("author", "Comment author cannot contain empty value", c.getAuthor()));
        }
        if (c.getAuthor() != null && c.getAuthor().length() > 15) {
            valResult.add(new BrokenRule("author", "Comment author contains too long value", c.getAuthor()));
        }
        boolean isSetEmail = (c.getEmail() != null && !c.getEmail().trim().equals(""));
        if (isSetEmail == true && c.getEmail().length() > 70) {
            valResult.add(new BrokenRule("email", "Email address is too long", c.getEmail()));
        }
        if (isSetEmail == true) {
            EmailValidationRule evr = new EmailValidationRule("email", c.getEmail());
            if (evr.validate().isValid() == false) {
                valResult.add(new BrokenRule("email", "Email address is not valid", c.getEmail()));
            }
        }
        boolean isSetWeb = (c.getWeb() != null && !c.getWeb().trim().equals(""));
        if (isSetWeb == true && c.getWeb().length() > 80) {
            valResult.add(new BrokenRule("web", "Web address contains too long URL", c.getWeb()));
        }
        if (isSetWeb == true) {
            UrlValidationRule uvr = new UrlValidationRule("web", c.getWeb());
            if (uvr.validate().isValid() == false) {
                valResult.add(new BrokenRule("web", "Web is not valid URL", c.getWeb()));
            }
        }
        if (c.getMessage() == null || c.getMessage().trim().equals("")) {
            valResult.add(new BrokenRule("message", "Message cannot contain empty value", c.getMessage()));
        }
        if (c.getCreated() == null) {
            valResult.add(new BrokenRule("created", "Comment must be inicialized with Date object"));
        }

        return valResult;
    }

    @Override
    public ValidationResult validateForUpdate(Comment arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}