package com.project.scm.controller;

import com.project.scm.models.Contacts;
import com.project.scm.services.ContactService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    private ContactService contactService;

    public ApiController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contacts/{contactId}")
    public Contacts getContacts(@PathVariable String contactId) {
        return contactService.getContactById(contactId);

    }
}


