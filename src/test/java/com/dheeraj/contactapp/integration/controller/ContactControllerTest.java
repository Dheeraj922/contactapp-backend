package com.dheeraj.contactapp.integration.controller;

import com.dheeraj.contactapp.domain.Contact;
import com.dheeraj.contactapp.repository.ContactRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.dheeraj.contactapp.integration.controller.utils.ContactControllerTestUtils.*;
import static com.dheeraj.contactapp.util.ContactTestUtils.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContactControllerTest extends ControllerIntegrationTest {
    private static final String INVALID_TEST_CONTACT = "";
    private static final String TEST_CONTACT = "{\"emailId\": \"dheerajchoudhary@gmail.com\", \"name\": \"Dheeraj Choudhary\", \"contactNumber\": \"9870179524\"}";
    private static final String TEST_CONTACT_MISSING_CONTACT_DATA = "{\"foo\": \"bar\"}";

    @Autowired
    private ContactRepository repository;

    @Before
    public void setUp() {
        repository.clear();
    }

    @Test
    public void testGetAllEmptyListEnsureCorrectResponse() throws Exception {
        assertNoContacts();
        getContact()
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[]")));
    }

    private ResultActions getContact() throws Exception {
        return get("/contact");
    }

    private void assertNoContacts() {
        assertContactCountIs(0);
    }

    private void assertContactCountIs(int count) {
           Assert.assertEquals(count, repository.getCount());
    }

    @Test
    public void testGetAllOneContactEnsureCorrectResponse() throws Exception {
        Contact injectedContact = injectContact();
        assertContactCountIs(1);
        getContact()
                .andExpect(status().isOk())
                .andExpect(contactAtIndexIsCorrect(0, injectedContact));
    }

    private Contact injectContact() throws Exception {
        Contact contact = new Contact();
        contact.setEmailId("test" + System.currentTimeMillis() + "@gmail.com");
        contact.setName("Test");
        contact.setContactNumber(123456789L);

       return repository.create(contact);
    }

    @Test
    public void testGetAllTwoContactEnsureCorrectResponse() throws Exception {
        Contact injectedContact1 = injectContact();
        Contact injectedContact2 = injectContact();
        assertContactCountIs(2);
        getContact()
                .andExpect(status().isOk())
                .andExpect(contactAtIndexIsCorrect(0, injectedContact1))
                .andExpect(contactAtIndexIsCorrect(1, injectedContact2));
    }

    @Test
    public void testGetNonexistentContactEnsureNotFoundResponse() throws Exception {
        assertNoContacts();
        getContact(1)
                .andExpect(status().isNotFound());
    }

    private ResultActions getContact(long id) throws Exception {
        return get("/contact/{id}", id);
    }

    @Test
    public void testGetExistingContactEnsureCorrectResponse() throws Exception {
        Contact injectedContact = injectContact();
        assertContactCountIs(1);
        getContact(injectedContact.getId())
                .andExpect(status().isOk())
                .andExpect(contactIsCorrect(injectedContact));
    }

    @Test
    public void testCreateNewContactEnsureContactCreated() throws Exception {
        assertNoContacts();
        Contact desiredContact = generateTestContact();
        createContact(toJsonString(desiredContact));
        assertContactCountIs(1);
        assertAllButIdsMatchBetweenContacts(desiredContact, getCreatedContact());
    }

    private ResultActions createContact(String payload) throws Exception {
        return post("/contact", payload);
    }

    private Contact getCreatedContact() {
        List<Contact> contacts = repository.search();
        return contacts.get(contacts.size() - 1);
    }

    @Test
    public void testCreateNewContactEnsureCorrectResponse() throws Exception {
        assertNoContacts();
        createContact(TEST_CONTACT)
                .andExpect(status().isCreated())
                .andExpect(contactIsCorrect(getCreatedContact()));
    }

    @Test
    public void testCreateNewContactMissingDataEnsureCorrectResponse() throws Exception {
        assertNoContacts();
        createContact(TEST_CONTACT_MISSING_CONTACT_DATA)
                .andExpect(status().isCreated())
                .andExpect(contactIsCorrect(getCreatedContact()));
    }

    @Test
    public void testCreateInvalidNewContactEnsureCorrectResponse() throws Exception {
        assertNoContacts();
        createContact(INVALID_TEST_CONTACT)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteNonexistentContactEnsureCorrectResponse() throws Exception {
        assertNoContacts();
        deleteContact(1)
                .andExpect(status().isNotFound());
    }

    private ResultActions deleteContact(long id) throws Exception {
        return delete("/contact/{id}", id);
    }

    @Test
    public void testUpdateNonexistentContactEnsureCorrectResponse() throws Exception {
        assertNoContacts();
        updateContact(1, new Contact())
                .andExpect(status().isNotFound());
    }

    private ResultActions updateContact(long id, Contact updatedContact) throws Exception {
        return put("/contact/{id}", updatedContact, String.valueOf(id));
    }

    @Test
    public void testUpdateExistingContactEnsureContactUpdated() throws Exception {
        Contact originalContact = injectContact();
        assertContactCountIs(1);
        Contact updatedContact = generateUpdatedContact(originalContact);
        updateContact(originalContact.getId(), updatedContact);
        assertAllButIdsMatchBetweenContacts(updatedContact, originalContact);
    }

    @Test
    public void testUpdateExistingContactEnsureCorrectResponse() throws Exception {
        Contact originalContact = injectContact();
        assertContactCountIs(1);
        Contact updatedContact = generateUpdatedContact(originalContact);
        updateContact(originalContact.getId(), updatedContact)
                .andExpect(status().isOk())
                .andExpect(updateContactIsCorrect(originalContact.getId(), updatedContact));
    }

}
