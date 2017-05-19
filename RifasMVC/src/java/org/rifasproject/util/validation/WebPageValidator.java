/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.util.validation;

import java.net.MalformedURLException;
import java.net.URL;
import org.rifasproject.domain.WebPage;
import sk.mortality.util.validation.AbstractValidator;
import sk.mortality.util.validation.BrokenRule;
import sk.mortality.util.validation.ValidationResult;

/**
 *
 * @author char0n
 */
public class WebPageValidator extends AbstractValidator<WebPage> {

    @Override
    public boolean isValidForSave(WebPage webPage) {

        ValidationResult valResult = this.validateForSave(webPage);

        return valResult.isValid();
    }

    @Override
    public boolean isValidForUpdate(WebPage webPage) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValidationResult validateForSave(WebPage webPage) {
        
        ValidationResult valResult = new ValidationResult();

        if (webPage.getContent() == null) {
            valResult.add(new BrokenRule("content", "WebPage must contain html content"));
        }
        if (webPage.getContent() != null && (webPage.getContent().trim().equals(""))) {
            valResult.add(new BrokenRule<String>("content", "WebPage contains empty content", webPage.getContent()));
        }
        if (webPage.getType() == null) {
            valResult.add(new BrokenRule("type", "WebPage must be associated with Mime-Type"));
        }
        if (webPage.getUrl() == null) {
            valResult.add(new BrokenRule("url", "WebPage must be associated with Url"));
        }
        if (webPage.getUrl() != null) {
            if (webPage.getUrl().length() > 800) {
                valResult.add(new BrokenRule<String>("url", "Url of WebPage is longer than 800 characters"));
            }

            try {
                URL url = new URL(webPage.getUrl());
            } catch (MalformedURLException ex) {
                valResult.add(new BrokenRule<String>("url", "WebPage must be associated with valid Url", webPage.getUrl()));
            }
        }

        return valResult;
    }

    @Override
    public ValidationResult validateForUpdate(WebPage webPage) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
