/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util.validation;

import java.net.MalformedURLException;
import java.net.URL;
import org.rifasproject.domain.Link;
import sk.mortality.util.validation.AbstractValidator;
import sk.mortality.util.validation.BrokenRule;
import sk.mortality.util.validation.ValidationResult;

/**
 *
 * @author char0n
 */
public class LinkValidator extends AbstractValidator<Link> {

    @Override
    public boolean isValidForSave(Link link) {
        
        ValidationResult valResult = this.validateForSave(link);
        return valResult.isValid();
    }

    @Override
    public boolean isValidForUpdate(Link link) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValidationResult validateForSave(Link link) {

        ValidationResult valResult = new ValidationResult();

        if (link.getUrl() == null) {
            valResult.add(new BrokenRule("url", "Link must be associated with Url"));
        }
        if (link.getUrl() != null) {
            if (link.getUrl().length() > 800) {
                valResult.add(new BrokenRule<String>("url", "Url of Link is longer than 800 characters"));
            }
            try {
                URL url = new URL(link.getUrl());
            } catch (MalformedURLException ex) {
                valResult.add(new BrokenRule<String>("url", "Link must be associated with valid Url"));
            }
        }

        return valResult;
    }

    @Override
    public ValidationResult validateForUpdate(Link link) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
