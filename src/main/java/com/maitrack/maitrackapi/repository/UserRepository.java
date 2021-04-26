package com.maitrack.maitrackapi.repository;

import com.maitrack.maitrackapi.domain.UnverifiedUser;
import com.maitrack.maitrackapi.domain.User;
import com.maitrack.maitrackapi.exceptions.MTAuthException;

public interface UserRepository {

    Integer create(String firstName, String lastName, String email, String password, String securityAnswer) throws MTAuthException;

    User findByEmailAndPassword(String email, String password) throws MTAuthException;

    Integer getCountByEmail(String email);

    String validateSecurity(String email, String securityAnswer, String newPassword);

    User findById(Integer userId);

    UnverifiedUser findUUByEmail(String email);

    String storeOTP(String firstName, String lastName, String email, String OTP);

    String findByCode(String OTP_input);

    void removeByCode(String OTP_input);

    void removeByEmail(String email);

    Integer findVerifRecordByEmail(String email);

    Boolean verifyByToken(String token, String email);

}
