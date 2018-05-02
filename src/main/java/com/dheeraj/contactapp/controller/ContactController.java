package com.dheeraj.contactapp.controller;

import com.dheeraj.contactapp.domain.Contact;
import com.dheeraj.contactapp.repository.ContactRepository;
import com.dheeraj.contactapp.resource.ContactResource;
import com.dheeraj.contactapp.resource.ContactResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@ExposesResourceFor(Contact.class)
@RequestMapping(value = "/contacts", produces = "application/json")
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactResourceAssembler assembler;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<ContactResource>> find(){
        List<Contact> contacts = contactRepository.search();
        return new ResponseEntity<>(assembler.toResourceCollection(contacts), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<ContactResource> createContact(@RequestBody Contact contact) {
        Contact createdContact;
        try {
            createdContact = contactRepository.create(contact);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(assembler.toResource(createdContact), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ContactResource> findContactById(@PathVariable Long id) {
        Optional<Contact> contact = contactRepository.findById(id);

        if (contact.isPresent()) {
            return new ResponseEntity<>(assembler.toResource(contact.get()), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/email", method = RequestMethod.GET)
    public ResponseEntity<ContactResource> findContactByEmailId(@RequestParam(value="emailId") String emailId) {
        Optional<Contact> contactToReturn;
        List<Contact> contacts = contactRepository.search();
        contactToReturn = contacts.stream().filter(e -> e.getEmailId().equals(emailId)).findFirst();
        if (contactToReturn.isPresent()) {
            return new ResponseEntity<>(assembler.toResource(contactToReturn.get()), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/name", method = RequestMethod.GET)
    public ResponseEntity<ContactResource> findContactByName(@RequestParam(value="name") String name) {
        Optional<Contact> contactToReturn;
        List<Contact> contacts = contactRepository.search();
        contactToReturn = contacts.stream().filter(e -> e.getName().equals(name)).findFirst();
        if (contactToReturn.isPresent()) {
            return new ResponseEntity<>(assembler.toResource(contactToReturn.get()), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        boolean wasDeleted = contactRepository.delete(id);
        HttpStatus responseStatus = wasDeleted ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(responseStatus);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<ContactResource> updateContact(@PathVariable Long id, @RequestBody Contact updatedOrder) {
        boolean wasUpdated = contactRepository.update(id, updatedOrder);

        if (wasUpdated) {
            return findContactById(id);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
