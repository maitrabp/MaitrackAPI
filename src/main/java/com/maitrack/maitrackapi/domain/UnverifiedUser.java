package com.maitrack.maitrackapi.domain;

public class UnverifiedUser {
    private String firstName;
    private String lastName;
    private String email;
    private String OTP;

    public UnverifiedUser(String firstName, String lastName, String email, String OTP) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.OTP = OTP;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }
}
