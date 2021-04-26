package com.maitrack.maitrackapi.services;

import com.maitrack.maitrackapi.domain.User;
import com.maitrack.maitrackapi.exceptions.MTAuthException;

public interface UserService {
    User validateUser(String email, String password) throws MTAuthException;
    User registerUser(String firstName, String lastName, String email, String password, String securityAnswer) throws MTAuthException;
    String checkSecurity(String email, String security, String newPassword);
    void verification(String firstName, String lastName, String email, String password, String securityAnswer);
    Boolean isValidToken(String token, String email);

}
