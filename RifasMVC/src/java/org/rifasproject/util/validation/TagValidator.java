/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util.validation;

import org.rifasproject.domain.Tag;
import sk.mortality.util.validation.AbstractValidator;
import sk.mortality.util.validation.BrokenRule;
import sk.mortality.util.validation.ValidationResult;

/**
 *
 * @author char0n
 */
public class TagValidator extends AbstractValidator<Tag> {

    @Override
    public boolean isValidForSave(Tag tag) {

        ValidationResult valResult = this.validateForSave(tag);

        return valResult.isValid();
    }

    @Override
    public boolean isValidForUpdate(Tag tag) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValidationResult validateForSave(Tag tag) {
        
        ValidationResult valResult = new ValidationResult();

        if (tag.getBinder() == null) {
            valResult.add(new BrokenRule("binder", "Tag contains null value"));
        }
        if (tag.getBinder() != null && (tag.getBinder().trim().equals("") || tag.getBinder().trim().length() > 80)) {
            valResult.add(new BrokenRule<String>("binder", "Tag contains invalid String value", tag.getBinder()));
        }
        if (tag.getCreated() == null) {
            valResult.add(new BrokenRule("created", "Tag must contain creation Date"));
        }

        return valResult;
    }

    @Override
    public ValidationResult validateForUpdate(Tag tag) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
