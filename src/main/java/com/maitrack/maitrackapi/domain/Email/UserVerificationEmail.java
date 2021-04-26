package com.maitrack.maitrackapi.domain.Email;

import com.maitrack.maitrackapi.domain.UnverifiedUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


public class UserVerificationEmail extends AbstractEmailContext{
    @Override
    public <T> void init(T context) {
        UnverifiedUser customer = (UnverifiedUser) context;
        put("firstName", customer.getFirstName());
        put("lastName", customer.getLastName());
        put("token", customer.getOTP());
        setTemplateLocation("email_template");
        setSubject("Registration: Email Verification");
        setFrom("noreply.maitrack@gmail.com");
        setTo(customer.getEmail());
    }
}
