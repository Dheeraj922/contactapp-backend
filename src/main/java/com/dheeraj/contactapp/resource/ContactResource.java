package com.dheeraj.contactapp.resource;

import com.dheeraj.contactapp.domain.Contact;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

public class ContactResource extends ResourceSupport {
    private final long id;
    private final String name;
    private final String emailId;
    private final long contactNumber;

    public ContactResource(Contact contact){
        id = contact.getId();
        name = contact.getName();
        emailId = contact.getEmailId();
        contactNumber = contact.getContactNumber();
    }

    @JsonProperty("id")
    public Long getResourceId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getEmailId(){
        return emailId;
    }
    public long getContactNumber(){
        return contactNumber;
    }
}
