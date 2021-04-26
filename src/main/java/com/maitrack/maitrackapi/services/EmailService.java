package com.maitrack.maitrackapi.services;

import com.maitrack.maitrackapi.domain.Email.AbstractEmailContext;

import javax.mail.MessagingException;

public interface EmailService {

    void sendMail(AbstractEmailContext email) throws MessagingException;
}
