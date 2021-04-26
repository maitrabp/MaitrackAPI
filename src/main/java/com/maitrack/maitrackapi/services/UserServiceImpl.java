package com.maitrack.maitrackapi.services;

import com.maitrack.maitrackapi.domain.Email.UserVerificationEmail;
import com.maitrack.maitrackapi.domain.UnverifiedUser;
import com.maitrack.maitrackapi.domain.User;
import com.maitrack.maitrackapi.exceptions.MTAuthException;
import com.maitrack.maitrackapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.mail.MessagingException;
import java.util.regex.Pattern;

@Service
@Transactional //For DB transactions using methods, whole trans. rolled out on runtime exception..

public class UserServiceImpl implements UserService{


    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Override
    public User validateUser(String email, String password) throws MTAuthException {
        //Emails are not case-sensitive
        if(email != null)
            email = email.toLowerCase();

        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public User registerUser(String firstName, String lastName, String email, String password, String securityAnswer) throws MTAuthException {
        //put in DB..
        Integer userId = userRepository.create(firstName, lastName, email, password, securityAnswer);
        //Grabs from DB & returns
        return userRepository.findById(userId);
    }
    public void emailValidationChecks(String email) {
        //Email Format Validation
        Pattern emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

        if(!emailPattern.matcher(email).matches())
            throw new MTAuthException("Invalid email format");

        Integer numEmailRecords = userRepository.getCountByEmail(email);
        if(numEmailRecords > 0)
            throw new MTAuthException("Email is already in use");
    }
    public void passwordValidationChecks(String password) {
        final String PASSWORD_PATTERN = "^" +
                "(?=.*[0-9])" + //# positive lookahead, digit [0-9]
                "(?=.*[a-z])" + //# positive lookahead, one lowercase character [a-z]
                "(?=.*[A-Z])" + //# positive lookahead, one uppercase character [A-Z]
                "(?=.*[!@#*&()–[{}]:;',?/*~$^+=<>])" + // # positive lookahead, one of the special character in this [..]
                "." +                        // # matches anything
                "{8,20}" + //# length at least 8 characters and maximum of 20 characters
                "$"; //# end of line
        final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        if(!pattern.matcher(password).matches()) {
            throw new MTAuthException("Password must contain:\n" +
                    "A digit [0-9],\n" +
                    "one lowercase character [a-z],\n" +
                    "one uppercase character [A-Z],\n" +
                    "one of the special character [!@#*&()],\n" +
                    "at least 8 characters and maximum of 20 characters,\n");
        }
    }

    @Override
    public String checkSecurity(String email, String securityAnswer, String newPassword) {

        if(securityAnswer != null && email != null) {
            securityAnswer = securityAnswer.toLowerCase();
            email = email.toLowerCase();
        }
        if(newPassword == null || newPassword.length() <= 0)
            throw new MTAuthException("New password cannot be blank");

        return userRepository.validateSecurity(email, securityAnswer, newPassword);
    }
    public String generateCode() {
        SecureRandom sc = new SecureRandom();
        int code = sc.nextInt(1000000);
        return String.format("%06d", code);
    }

    @Override
    public void verification(String firstName, String lastName, String email, String password, String securityAnswer) {
        //******************************EMAIL VALIDATION**********************
        if(email != null) {
            email = email.toLowerCase();
            emailValidationChecks(email);
        }
        else
            throw new MTAuthException("Email must not be empty");
        //******************************PASSWORD VALIDATION**********************
        if(password != null)
            passwordValidationChecks(password);
        else
            throw new MTAuthException("Password must not be empty");
        //******************************SECURITY ANSWER VALIDATION***************
        if(securityAnswer != null)
            securityAnswer = securityAnswer.toLowerCase();
        else
            throw new MTAuthException("Security question must not be empty");

        String code = generateCode();

        //Remove if record already exists
        if(userRepository.findVerifRecordByEmail(email) > 0) {
            userRepository.removeByEmail(email);
        }
        //set token
        String key = userRepository.storeOTP(firstName, lastName, email, code);

        if(key != null) {
            UnverifiedUser user = userRepository.findUUByEmail(key);
            //send email with user context...
            UserVerificationEmail emailContext = new UserVerificationEmail();
            emailContext.init(user);
            try{
                emailService.sendMail(emailContext);
            } catch(MessagingException e){
                throw new MTAuthException(e.getMessage());
            }
        } else {
            throw new MTAuthException("Error: Unable to store verification code in the database.");
        }
    }

    @Override
    public Boolean isValidToken(String code, String email) {
        boolean isValid = false;
        if(code == null)
            throw new MTAuthException("Please provide the verification code!");
        else if(email == null)
            throw new MTAuthException("Error: Email must be provided to check the registration token");
        else {
            email = email.toLowerCase();
            isValid = userRepository.verifyByToken(code, email);

            if(isValid)
                userRepository.removeByEmail(email);
        }
        return isValid;
    }
}
