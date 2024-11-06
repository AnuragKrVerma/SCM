package com.project.scm.services.impl;

import com.project.scm.helper.ResourceNotFoundException;
import com.project.scm.models.Contacts;
import com.project.scm.models.User;
import com.project.scm.repositories.ContactRepository;
import com.project.scm.repositories.UserRepository;
import com.project.scm.services.ContactService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    public ContactServiceImpl(ContactRepository contactRepository, UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Contacts saveContact(Contacts contact) {
        User user = userRepository.findById(contact.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + contact.getUser().getId()));
        contact.setUser(user);
        contact.setId(UUID.randomUUID().toString());
        return contactRepository.save(contact);
    }

    @Override
    public List<Contacts> getAll() {
        return List.of();
    }

    @Override
    public void updateContact(Contacts contact) {
        // Implementation here
    }

    @Override
    public void deleteContact(String id) {
        var contact = contactRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id " + id));
        contactRepository.delete(contact);
    }

    @Override
    public Contacts getContactById(String id) {
        return contactRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id " + id));
    }

    @Override
    public Page<Contacts> searchByName(String name, int size, int page, String sortBy, String direction) {

        Sort sort = direction.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByNameContaining(name, pageable);
    }

    @Override
    public Page<Contacts> searchByPhoneNumber(String phoneNumber, int size, int page, String sortBy, String direction) {
        Sort sort = direction.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByPhoneNumberContaining(phoneNumber, pageable);
    }

    @Override
    public Page<Contacts> searchByEmail(String email, int size, int page, String sortBy, String direction) {
        Sort sort = direction.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByEmailContaining(email, pageable);
    }


    @Override
    public List<Contacts> getAllContactsByUserId(String userId) {
        return contactRepository.findByUserId(userId);
    }

    @Override
    public Page<Contacts> getByUser(User user, int page, int size, String sortBy, String direction) {

        Sort sort = direction.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        return contactRepository.findByUser(user, PageRequest.of(page, size, sort));
    }


}