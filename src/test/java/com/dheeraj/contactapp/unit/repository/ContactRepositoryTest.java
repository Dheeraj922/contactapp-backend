package com.dheeraj.contactapp.unit.repository;
import static com.dheeraj.contactapp.util.ContactTestUtils.*;

import com.dheeraj.contactapp.domain.Contact;
import com.dheeraj.contactapp.repository.ContactRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContactRepositoryTest {

    private static final long NONEXISTENT_ID = 1000;

    @Autowired
    private ContactRepository repository;

    @Before
    public void setUp() {
        repository.clear();
    }

    @Test
    public void testFindNonexistentContactEnsureOptionalIsNotPresent() throws Exception {
        assertNoExistingContacts();
        Optional<Contact> contact = repository.findById(NONEXISTENT_ID);
        Assert.assertFalse(contact.isPresent());
    }

    private void assertNoExistingContacts() {
        assertExistingContactCountIs(0);
    }

    private void assertExistingContactCountIs(int count) {
        Assert.assertEquals(count, repository.getCount());
    }

    @Test
    public void testFindExistingContactEnsureOptionalIsPresent() throws Exception {
        Contact injectedContact = injectContact();
        Optional<Contact> foundContact = repository.findById(injectedContact.getId());
        Assert.assertTrue(foundContact.isPresent());
    }

    private Contact injectContact() {
        Contact createdContact = null;
        try {
            createdContact = repository.create(generateTestContact());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createdContact;
    }

    @Test
    public void testFindExistingContactEnsureCorrectContactValues() throws Exception {
        Contact injectedContact = injectContact();
        Optional<Contact> foundContact = repository.findById(injectedContact.getId());
        assertContactsMatch(injectedContact, foundContact.get());
    }

    private static void assertContactsMatch(Contact expected, Contact actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        assertAllButIdsMatchBetweenContacts(expected, actual);
    }

    @Test
    public void testFindAllWithNoExistingContactsEnsureNoContactsFound() throws Exception {
        assertFindAllIsCorrectWithContactCount(0);
    }

    private void assertFindAllIsCorrectWithContactCount(int count) {
        injectGivenNumberOfContacts(count);
        assertExistingContactCountIs(count);
        List<Contact> contactsFound = repository.search();
        Assert.assertEquals(count, contactsFound.size());
    }

    private List<Contact> injectGivenNumberOfContacts(int count) {

        List<Contact> injectedContacts = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            injectedContacts.add(injectContact());
        }

        return injectedContacts;
    }

    @Test
    public void testFindAllWithOneExistingContactsEnsureOneContactFound() throws Exception {
        assertFindAllIsCorrectWithContactCount(1);
    }

    @Test
    public void testFindAllWithTwoExistingContactsEnsureTwoContactsFound() throws Exception {
        assertFindAllIsCorrectWithContactCount(2);
    }

    @Test
    public void testFindAllWithTwoExistingContactEnsureFirstContactIsCorrect() throws Exception {
        List<Contact> injectedContacts = injectGivenNumberOfContacts(2);
        List<Contact> contactsFound = repository.search();
        assertContactsMatch(injectedContacts.get(0), contactsFound.get(0));
    }

    @Test
    public void testFindAllWithTwoExistingContactEnsureSecondContactIsCorrect() throws Exception {
        List<Contact> injectedContacts = injectGivenNumberOfContacts(2);
        List<Contact> contactsFound = repository.search();
        assertContactsMatch(injectedContacts.get(1), contactsFound.get(1));
    }

    @Test
    public void testDeleteNonexistentContactEnsureNoContactDeleted() throws Exception {
        assertNoExistingContacts();
        boolean wasDeleted = repository.delete(NONEXISTENT_ID);
        Assert.assertFalse(wasDeleted);
    }

    @Test
    public void testDeleteExistingContactEnsureContactDeleted() throws Exception {
        Contact injectedContact = injectContact();
        assertExistingContactCountIs(1);
        boolean wasDeleted = repository.delete(injectedContact.getId());
        Assert.assertTrue(wasDeleted);
        assertNoExistingContacts();
    }

    @Test
    public void testUpdateNonexistentContactEnsureNoUpdateMade() throws Exception {
        assertNoExistingContacts();
        boolean wasUpdated = repository.update(NONEXISTENT_ID, new Contact());
        Assert.assertFalse(wasUpdated);
    }

    @Test
    public void testUpdateExistingContactEnsureUpdateMade() throws Exception {
        Contact injectedContact = injectContact();
        boolean wasUpdated = repository.update(injectedContact.getId(), new Contact());
        Assert.assertTrue(wasUpdated);
    }

    @Test
    public void testUpdateExistingContactEnsureOriginalContactIsUpdated() throws Exception {
        Contact originalContact = injectContact();
        Contact updatedContact = generateUpdatedContact(originalContact);
        repository.update(originalContact.getId(), updatedContact);
        assertAllButIdsMatchBetweenContacts(updatedContact, originalContact);
    }

    @Test
    public void testUpdateExistingContactWithNullUpdatedContactEnsureNoUpdateMade() throws Exception {
        Contact injectedContact = injectContact();
        boolean wasUpdated = repository.update(injectedContact.getId(), null);
        Assert.assertFalse(wasUpdated);
    }


}
