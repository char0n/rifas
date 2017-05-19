/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util.validation;

import org.rifasproject.domain.LinkSet;
import sk.mortality.util.validation.AbstractValidator;
import sk.mortality.util.validation.BrokenRule;
import sk.mortality.util.validation.ValidationResult;

/**
 *
 * @author char0n
 */
public class LinkSetValidator extends AbstractValidator<LinkSet> {

    @Override
    public boolean isValidForSave(LinkSet linkSet) {
        
        ValidationResult valResult = this.validateForSave(linkSet);
        return valResult.isValid();
    }

    @Override
    public boolean isValidForUpdate(LinkSet linkSet) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValidationResult validateForSave(LinkSet linkSet) {

        ValidationResult valResult = new ValidationResult();

        if (linkSet.getUuid() == null) {
            valResult.add(new BrokenRule("uuid", "LinkSet must contain uuid hash"));
        }
        if (linkSet.getUuid() != null && linkSet.getUuid().trim().length() != 40) {
            valResult.add(new BrokenRule<String>("uuid", "LinkSet contains invalid uuid", linkSet.getUuid()));
        }
        if (linkSet.getName() != null && linkSet.getName().length() > 80) {
            valResult.add(new BrokenRule<String>("name", "LinkSet contains too long name", linkSet.getName()));
        }
        if (linkSet.getDescription() != null && linkSet.getDescription().length() > 200) {
            valResult.add(new BrokenRule<String>("description", "LinkSet contains too long description", linkSet.getDescription()));
        }
        if (linkSet.getType() == null) {
            valResult.add(new BrokenRule("type", "LinkSet must by associated with LinkSetType"));
        }
        if (linkSet.getStorage() == null) {
            valResult.add(new BrokenRule("storage", "LinkSet must by associated with InternetStorage"));
        }
        if (linkSet.getSource() == null) {
            valResult.add(new BrokenRule("source", "LinkSet must by associated with LinkSetSource"));
        }
        if (linkSet.getCreated() == null) {
            valResult.add(new BrokenRule("created", "LinkSet must contain creation Date"));
        }

        return valResult;
    }

    @Override
    public ValidationResult validateForUpdate(LinkSet linkSet) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
