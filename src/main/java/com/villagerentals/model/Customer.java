package com.villagerentals.model;

public class Customer {
    private int customerId;
    private String lastName;
    private String firstName;
    private String contactPhone;
    private String email;

    public Customer() {}

    public Customer(int customerId, String lastName, String firstName, String contactPhone, String email) {
        this.customerId = customerId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.contactPhone = contactPhone;
        this.email = email;
    }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return customerId + " - " + firstName + " " + lastName;
    }
}
