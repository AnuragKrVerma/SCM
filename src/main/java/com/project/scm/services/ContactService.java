package com.project.scm.services;

import com.project.scm.models.Contacts;
import com.project.scm.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContactService {
    //    save contact
    Contacts saveContact(Contacts contact);

    //    get all contacts
    List<Contacts> getAll();

    //    update contact
    void updateContact(Contacts contact);

    //    delete contact
    void deleteContact(String id);

    //    get contact by id
    Contacts getContactById(String id);

    //    search contact
    Page<Contacts> searchByName(String name, int size, int page, String sortBy, String direction);

    Page<Contacts> searchByPhoneNumber(String phoneNumber, int size, int page, String sortBy, String direction);

    Page<Contacts> searchByEmail(String email, int size, int page, String sortBy, String direction);

    //    get all contacts by user id
    List<Contacts> getAllContactsByUserId(String userId);

//    Page<Contacts> getByUser(User user, int page, int size);

    Page<Contacts> getByUser(User user, int page, int size, String sortBy, String direction);
}
