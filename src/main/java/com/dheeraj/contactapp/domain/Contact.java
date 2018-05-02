package com.dheeraj.contactapp.domain;

public class Contact implements Identifiable {

    private Long id;
    private String emailId;
    private String name;
    private long contactNumber;


    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(Long contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmailId() {
        return emailId;

    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @Override
    public Long getId() {
        return id;
    }
}
