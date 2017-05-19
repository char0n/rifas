/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.mvc;

import org.json.JSONException;
import org.json.JSONObject;
import org.rifasproject.domain.command.ContactForm;
import org.rifasproject.services.ContactService;
import org.rifasproject.services.ServiceException;
import org.rifasproject.util.validation.ContactFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import sk.mortality.util.validation.ValidationResult;

/**
 *
 * @author char0n
 */
@Controller
@RequestMapping("/contact/*")
public class ContactController {

    @Autowired private ContactFormValidator contactFormValidator;
    @Autowired private ContactService contactService;    

    @RequestMapping(value="/contact/contactinfo.htm", method=RequestMethod.GET)
    public ModelAndView contactInfo() throws ServiceException {

        BaseModelAndView mav = new BaseModelAndView();
        mav.setPageTitle("Contact");
        mav.setPageDescription("Contact");
        mav.setPageKeywords("contact");
        mav.addObject("activeMenuItem", "contact");


        return mav;
    }

    @RequestMapping(value="/contact/sendEmail.htm", method=RequestMethod.POST)
    public ModelAndView sendEmail(@ModelAttribute("contactForm") ContactForm form) throws ServiceException {
        
        JSONObject json = null;
        ValidationResult valResult;
        ModelAndView mav = new ModelAndView();

        json  = new JSONObject();
        try {
            
            valResult = this.contactFormValidator.validateForSave(form);
            if (valResult.isValid() == true) {
                this.contactService.sendMail(form);
                json.append("Status", "OK");
                mav.addObject("json", json.toString(1));                
            } else {
                json.append("Status", "KO");
                json.append("Error", valResult.getBrokenRules().get(0).getErrorDescription());
                mav.addObject("json", json.toString(1));
            }
        } catch (JSONException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }

        return mav;
    }

}
