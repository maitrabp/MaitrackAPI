/*WITHIN THIS FILE WE HANDLE API CALLS*/

package com.maitrack.maitrackapi.resources;


import com.maitrack.maitrackapi.JWTProperties;
import com.maitrack.maitrackapi.domain.User;
import com.maitrack.maitrackapi.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/users")
public class UserResource {

    @Autowired
    UserService userService;

    @RequestMapping("/email_template")
    public String email() {
        return "email_template";
    }

    @PostMapping("/login") //"message":"something" is <String, String> for Map
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, Object> userMap){
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");

        User user = userService.validateUser(email, password);

        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }


    @PostMapping("/register") //ResponseEntity allows you to throw http errors, pass headers etc
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, Object> userMap) {
        String firstName = (String) userMap.get("firstName");
        String lastName = (String) userMap.get("lastName");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        String securityAnswer = (String) userMap.get("securityAnswer");
        User user = userService.registerUser(firstName, lastName, email, password, securityAnswer);
        Map<String, String> result = new HashMap<>();
        result.put("message", "Successfully registered!");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private Map<String, String> generateJWTToken(User user) {
        long timestamp = System.currentTimeMillis();
        //Using HASHED SHA we make signature
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, JWTProperties.getSECRET_KEY())
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + JWTProperties.getTOKEN_VALIDITY()))
                .claim("userId", user.getUserId()) //don't pass secure data here such as password (to generate token)
                .claim("email", user.getEmail())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .compact();
        token = myEncrypt(token);
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }

    private String myEncrypt(String t){
        Random r = new Random();
        char x = (char)(r.nextInt(26) + 'a');

        t = "e" + t + "x";

        return t;
    }

    @PostMapping("/passwordChangeValidation")
    public ResponseEntity securityCheck(@RequestBody Map<String, Object> userMap){
        String email = (String) userMap.get("email");
        String security_answer = (String) userMap.get("securityAnswer");
        String newPassword = (String) userMap.get("newPassword");

        String firstName = userService.checkSecurity(email, security_answer, newPassword);

        Map<String, String> map = new HashMap<>();
        if(firstName != null){
            map.put("message", firstName + ", you can now login to your account with your new password!");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            map.put("message", "Error: Cannot change your password at this moment. Contact support team");
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/verification")
    public ResponseEntity verifyUser(@RequestBody Map<String, Object> userMap){

        String firstName = (String) userMap.get("firstName");
        String lastName = (String) userMap.get("lastName");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        String securityAnswer = (String) userMap.get("securityAnswer");

        userService.verification(firstName, lastName, email, password, securityAnswer);
        Map<String, String> map = new HashMap<>();
        map.put("message", "Please check your email for a verification code!");

        return new ResponseEntity(map, HttpStatus.OK);
    }
    @PostMapping("/validateCode")
    public ResponseEntity validateRegistrant(@RequestBody @Valid Map<String, Object> userMap){
        String code = (String) userMap.get("code");
        String email = (String) userMap.get("email");
        Map<String, String> map = new HashMap<>();

        if(userService.isValidToken(code, email))
            map.put("message", "Email successfully verified!");
        else
            map.put("error", "Invalid token provided. Try again!");

        return new ResponseEntity(map, HttpStatus.OK);
    }
}
