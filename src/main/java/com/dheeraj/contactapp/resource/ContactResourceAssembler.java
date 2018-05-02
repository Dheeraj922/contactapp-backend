package com.dheeraj.contactapp.resource;

import com.dheeraj.contactapp.domain.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class ContactResourceAssembler extends ResourceAssembler<Contact, ContactResource> {

    @Autowired
    protected EntityLinks entityLinks;

    private static final String UPDATE_REL = "update";
    private static final String DELETE_REL = "delete";

    @Override
    public ContactResource toResource(Contact contact) {
        ContactResource resource = new ContactResource(contact);
        final Link selfLink = entityLinks.linkToSingleResource(contact);
        resource.add(selfLink.withSelfRel());
        resource.add(selfLink.withRel(UPDATE_REL));
        resource.add(selfLink.withRel(DELETE_REL));
        return resource;
    }
}
