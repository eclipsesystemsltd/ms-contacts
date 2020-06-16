package uk.co.meridenspares.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.co.meridenspares.domain.Contact;
import uk.co.meridenspares.service.api.ContactService;
import uk.co.meridenspares.service.api.exception.MsServiceException;
import uk.co.meridenspares.web.form.Message;
import uk.co.meridenspares.web.util.UrlUtil;

@RequestMapping("/contacts")
@Controller
public class ContactController {
	
	private final Logger log = Logger.getLogger(ContactController.class);
	
	@Autowired
	private ContactService contactService;
	
	@Autowired
	private MessageSource messageSource;

	@RequestMapping(method = RequestMethod.GET)
	public String list(Model uiModel) {
		log.info("Listing contacts");
		
		List<Contact> contacts = new ArrayList<Contact>();
		
		try {
			contacts = contactService.findAll();
		}
		catch (MsServiceException e) {
			log.error("Exception thrown with message: " + e.getMessage());
		}
		
		uiModel.addAttribute("contacts", contacts);
		
		for (Contact contact : contacts) {
			if (contact.getLastContactedDate() != null) System.out.println("X= " + contact.getLastContactedDate().toString());
		}
		
		log.info("No of contacts = " + contacts.size());
		
		return "contacts/list";
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public String show(@PathVariable("id") Long id, Model uiModel) {
		Contact contact;
		
		try {
			contact = contactService.find(id);
			uiModel.addAttribute("contact", contact);
		}
		catch (MsServiceException e) {
			e.printStackTrace();
		}
		return "contacts/show";
	}
	
	@RequestMapping(value="/{id}", params = "form", method=RequestMethod.POST)
	public String update(@Valid Contact contact, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest,
					RedirectAttributes redirectAttributes, Locale locale) {
		log.info("Updating contact");
		
		if (bindingResult.hasErrors()) {
			Message msg = new Message("error", messageSource.getMessage("contact_save_fail", new Object[]{}, locale));
			uiModel.addAttribute("message", msg);
			return "contacts/update";
		}
		
		uiModel.asMap().clear();
		Message msg = new Message("success", messageSource.getMessage("contact_save_success", new Object[]{}, locale));
		redirectAttributes.addFlashAttribute("message", msg);
		
		try {
			//TODO check whether need both?
			if (contact.getId() == null) {
				contact = contactService.save(contact);
				log.info("update - adding new contact");
			}
			else {
				contact = contactService.update(contact);
				log.info("update - updating contact with ID [" + contact.getId() + "]");
			}
		}
		catch (MsServiceException e) {
			log.warn("Failed to save contact: " + e.getMessage());
		}

		return "redirect:/contacts/" + UrlUtil.encodeUrlPathSegment(contact.getId().toString(), httpServletRequest);
	}

	@RequestMapping(value="/{id}", params = "form", method=RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		try {
			uiModel.addAttribute("contact", contactService.find(id));
		}
		catch (MsServiceException e) {
			log.warn("Failed to updateForm with contact: " + e.getMessage());
		}
		
		return "contacts/update";
	}
	
	@RequestMapping(params = "form", method=RequestMethod.POST)
	public String create(@Valid Contact contact, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest,
					RedirectAttributes redirectAttributes, Locale locale) {
		log.info("Creating contact");
		
		if (bindingResult.hasErrors()) {
			Message msg = new Message("error", messageSource.getMessage("contact_save_fail", new Object[]{}, locale));
			uiModel.addAttribute("message", msg);
			uiModel.addAttribute("contact", contact);
			return "contacts/create";
		}
		
		uiModel.asMap().clear();
		Message msg = new Message("success", messageSource.getMessage("contact_save_success", new Object[]{}, locale));
		redirectAttributes.addFlashAttribute("message", msg);
		
		log.info("Contact info: " + contact.toString());
		
		try {
			//TODO check whether need both?
			if (contact.getId() == null) {
				contact = contactService.save(contact);
				log.info("create - adding new contact");
			}
			else {
				contact = contactService.update(contact);
				log.info("create - updating contact with ID [" + contact.getId() + "]");
			}
		}
		catch (MsServiceException e) {
			log.warn("Failed to save contact: " + e.getMessage());
		}

		return "redirect:/contacts/" + UrlUtil.encodeUrlPathSegment(contact.getId().toString(), httpServletRequest);
	}

	@RequestMapping(params = "form", method=RequestMethod.GET)
	public String createForm(Model uiModel) {
		Contact contact = new Contact();
		uiModel.addAttribute("contact", contact);
		return "contacts/create";
	}

}
