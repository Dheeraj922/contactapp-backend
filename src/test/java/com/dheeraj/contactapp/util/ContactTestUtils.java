package com.dheeraj.contactapp.util;

import com.dheeraj.contactapp.domain.Contact;
import org.junit.Assert;

public class ContactTestUtils {
    public static void assertAllButIdsMatchBetweenContacts(Contact expected, Contact actual) {
        Assert.assertEquals(expected.getEmailId(), actual.getEmailId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getContactNumber(), actual.getContactNumber());
    }

    public static Contact generateTestContact() {
        Contact contact = new Contact();
        contact.setEmailId("dheerajchoudhary" + System.currentTimeMillis() + "@gmail.com");
        contact.setName("Dheeraj Choudhary");
        contact.setContactNumber(1234567890L);
        return contact;
    }

    public static Contact generateUpdatedContact(Contact original) {
        Contact updated = new Contact();
        updated.setName(original.getName() + " Updated");
        updated.setContactNumber(9876543210L);
        return updated;
    }
}
