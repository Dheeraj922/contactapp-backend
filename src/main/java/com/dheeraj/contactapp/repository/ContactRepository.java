package com.dheeraj.contactapp.repository;

import com.dheeraj.contactapp.domain.Contact;
import org.springframework.stereotype.Repository;

@Repository
public class ContactRepository extends InMemoryRepository<String, Contact> {
    @Override
    protected void updateIfExists(Contact original, Contact updated) {
        original.setContactNumber(updated.getContactNumber());
        original.setEmailId(updated.getEmailId());
        original.setName(updated.getName());
    }

    public  String  getMapKey(Contact contact){
        return contact.getEmailId();
    }
}
