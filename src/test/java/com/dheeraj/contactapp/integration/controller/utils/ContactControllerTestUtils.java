package com.dheeraj.contactapp.integration.controller.utils;

import com.dheeraj.contactapp.domain.Contact;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ContactControllerTestUtils {
    public static ResultMatcher contactAtIndexIsCorrect(int index, Contact expected) {
        return new CompositeResultMatcher()
                .addMatcher(jsonPath("$.[" + index + "].id").value(expected.getId()))
                .addMatcher(jsonPath("$.[" + index + "].emailId").value(expected.getEmailId()))
                .addMatcher(jsonPath("$.[" + index + "].name").value(expected.getName()))
                .addMatcher(jsonPath("$.[" + index + "].contactNumber").value(expected.getContactNumber()));
    }

    public static ResultMatcher contactIsCorrect(Contact expected) {
        return contactIsCorrect(expected.getId(), expected);
    }

    private static ResultMatcher contactIsCorrect(Long expectedId, Contact expected) {
        return new CompositeResultMatcher().addMatcher(jsonPath("$.id").value(expectedId))
                .addMatcher(jsonPath("$.emailId").value(expected.getEmailId()))
                .addMatcher(jsonPath("$.name").value(expected.getName()))
                .addMatcher(jsonPath("$.contactNumber").value(expected.getContactNumber()));
    }

    public static ResultMatcher updateContactIsCorrect(Long originalId, Contact expected) {
        return contactIsCorrect(originalId, expected);
    }
}
