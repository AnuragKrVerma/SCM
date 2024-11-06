package com.project.scm.controller;

import com.project.scm.forms.ContactForm;
import com.project.scm.helper.AppConstants;
import com.project.scm.helper.GetEmailOfLoggedInUser;
import com.project.scm.helper.Message;
import com.project.scm.helper.MessageType;
import com.project.scm.models.Contacts;
import com.project.scm.models.User;
import com.project.scm.services.ContactService;
import com.project.scm.services.ImageService;
import com.project.scm.services.impl.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    private final ContactService contactService;
    private final UserServiceImpl userServiceImpl;
    private final ImageService imageService;

    public ContactController(ContactService contactService, UserServiceImpl userServiceImpl, ImageService imageService) {
        this.contactService = contactService;
        this.userServiceImpl = userServiceImpl;
        this.imageService = imageService;
    }

    @RequestMapping("/add")
    public String addContactView(Model model) {
        logger.info("Entering addContactView method");

        ContactForm contactForm = new ContactForm();


        model.addAttribute("contactForm", contactForm);

        logger.info("Exiting addContactView method");
        return "user/add-contact";
    }

    @PostMapping("/add")
    public String addContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult bindingResult, Authentication authentication, Model model, HttpSession session) {
        logger.info("Entering addContact method with contactForm: {}", contactForm);

        if (bindingResult.hasErrors()) {

            logger.error("Error in binding result: {}", bindingResult);
            session.setAttribute("message", Message.builder()
                    .content("Please fill all the required fields")
                    .type(MessageType.red)
                    .build());
            model.addAttribute("contactForm", contactForm);
            return "user/add-contact";
        }

        String fileName = UUID.randomUUID().toString();

        String fileURL = imageService.uploadImage(contactForm.getProfileImage(), fileName);

        logger.info("file information : {}", contactForm.getProfileImage().getOriginalFilename());


        String email = GetEmailOfLoggedInUser.getEmail(authentication);
        logger.info("User profile request received is  {}", email);
        User user = userServiceImpl.getUserByEmail(email);

        logger.info("User  is   {}", user);
        if (user == null) {
            logger.error("User not found with email: {}", email);
            throw new EntityNotFoundException("User not found with email: " + email);
        }

        Contacts contact = new Contacts();
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setFavorite(contactForm.isFavorite());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setUser(user);
        contact.setCloudinaryImagePublicId(fileName);
        contact.setPicture(fileURL);

        contactService.saveContact(contact);

        logger.info("Exiting addContact method");
        session.setAttribute("message", Message.builder().content("User Registered Successfully").type(MessageType.green).build());
        return "redirect:/user/contacts/add";
    }

    @RequestMapping("/view-contacts")
    public String viewContacts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model, Authentication authentication) {
        logger.info("Entering viewContacts method");

        String email = GetEmailOfLoggedInUser.getEmail(authentication);
        logger.info("User profile request received is  {}", email);
        User user = userServiceImpl.getUserByEmail(email);

        logger.info("User  is   {}", user);
        if (user == null) {
            logger.error("User not found with email: {}", email);
            throw new EntityNotFoundException("User not found with email: " + email);
        }

        Page<Contacts> pageContacts = contactService.getByUser(user, page, size, sortBy, direction);
        model.addAttribute("pageContacts", pageContacts);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        logger.info("Exiting viewContacts method");
        return "user/view-contacts";
    }

    //    search handler
    @RequestMapping("/search")
    public String searchHandler(
            @RequestParam(value = "field") String field,
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model
    ) {
        Page<Contacts> pageContacts = null;
        if (field.equalsIgnoreCase("name")) {
            pageContacts = contactService.searchByName(keyword, size, page, sortBy, direction);
        } else if (field.equalsIgnoreCase("phone")) {
            pageContacts = contactService.searchByPhoneNumber(keyword, size, page, sortBy, direction);
        } else if (field.equalsIgnoreCase("email")) {
            pageContacts = contactService.searchByEmail(keyword, size, page, sortBy, direction);
        }

        logger.info("field: {} keyword: {}", field, keyword);

        logger.info("pageContacts: {}", pageContacts);
        model.addAttribute("pageContacts", pageContacts);

        return "user/search";
    }


}