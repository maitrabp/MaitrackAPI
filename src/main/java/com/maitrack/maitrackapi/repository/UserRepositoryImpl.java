/* WITHIN THIS FILE WE ACTUALLY MAKE TRANSACTIONS WITH THE DATABASE FOR THE USER TABLE*/

package com.maitrack.maitrackapi.repository;

import com.maitrack.maitrackapi.domain.User;
import com.maitrack.maitrackapi.domain.UnverifiedUser;
import com.maitrack.maitrackapi.exceptions.MTAuthException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class UserRepositoryImpl implements UserRepository{

    //Queries (? = PreparedStatements, value to be passed later)
    private static final String SQL_CREATE = "INSERT INTO MT_USERS(USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, SECURITY_ANSWER) VALUES(NEXTVAL('MT_USERS_SEQ'), ?, ?, ?, ?, ?)";
    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM MT_USERS WHERE EMAIL = ?";
    private static final String SQL_FIND_BY_ID = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, SECURITY_ANSWER FROM MT_USERS WHERE USER_ID = ?";
    private static final String SQL_FIND_BY_EMAIL = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, SECURITY_ANSWER FROM MT_USERS WHERE EMAIL = ?";
    private static final String SQL_UPDATE_PASSWORD = "UPDATE MT_USERS SET PASSWORD = ? WHERE EMAIL = ?";

    //Unverified User
    private static final String SQL_CREATE_OTP = "INSERT INTO MT_VERIFY(FIRST_NAME, LAST_NAME, EMAIL, OTP) VALUES(?, ?, ?, ?);";
    private static final String SQL_FIND_BY_OTP = "SELECT EMAIL FROM MT_VERIFY WHERE OTP = ?";
    private static final String SQL_OTP_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM MT_VERIFY WHERE EMAIL = ?";
    private static final String SQL_DELETE_BY_OTP = "DELETE FROM MT_VERIFY WHERE OTP = ?";
    private static final String SQL_DELETE_BY_EMAIL = "DELETE FROM MT_VERIFY WHERE EMAIL = ?";
    private static final String SQL_UU_FIND_BY_EMAIL = "SELECT EMAIL, OTP, FIRST_NAME, LAST_NAME FROM MT_VERIFY WHERE EMAIL = ?";
    private static final String SQL_UU_FIND_BY_EMAIL_AND_TOKEN = "SELECT COUNT(*) FROM MT_VERIFY WHERE OTP = ? AND EMAIL = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Integer create(String firstName, String lastName, String email, String password, String securityAnswer) throws MTAuthException {
        //Salt (Random bytes, combines password value with salt --> 2 same passwords for diff users will still be unique)
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        String hashedSecKey = BCrypt.hashpw(securityAnswer, BCrypt.gensalt(5));
        try {
            //to store userId which is retrieved back after putting the record
            KeyHolder keyHolder = new GeneratedKeyHolder();
            //lambda function for adding the record to the db
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS); //here newly generated key will be USER_ID
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3,email);
                ps.setString(4, hashedPassword);
                ps.setString(5, hashedSecKey);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("USER_ID"); //returns back the id
        } catch(Exception e) {
            throw new MTAuthException("Invalid details. Failed to create an account");
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws MTAuthException {
        try{
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, userRowMapper, new Object[]{email});

            //Validate the encrypted password (passed vs the one in DB)
            if(!BCrypt.checkpw(password, user.getPassword())) {
                throw new MTAuthException("Invalid email or password");
            } else {
                return user;
            }

        } catch(EmptyResultDataAccessException e) {
            throw new MTAuthException("Invalid email or password");
        }
    }

    @Override
    public Integer getCountByEmail(String email) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, Integer.class, new Object[]{email});
    }

    @Override
    public String validateSecurity(String email, String securityAnswer, String newPassword) {
        try {
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, userRowMapper, new Object[]{email});
            //Validate the encrypted securityAnswer (passed vs the one in DB)
            if(!BCrypt.checkpw(securityAnswer, user.getSecurityAnswer())) {
                throw new MTAuthException("Invalid answer! Try again");
            } else {
                String firstName = updatePassword(email, newPassword);
                if(firstName != null)
                    return firstName;
                else
                    return null;
            }
        } catch(EmptyResultDataAccessException e) {
            throw new MTAuthException("Invalid email address! Try again");
        }
    }

    //userRowMapper is used to get records of verified user
    @Override
    public User findById(Integer userId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, userRowMapper, new Object[]{userId});
    }

    public UnverifiedUser findUUByEmail(String email) {
        return jdbcTemplate.queryForObject(SQL_UU_FIND_BY_EMAIL, unverifiedUserRowMapper, new Object[]{email});
    }

    //store the email verification token in DB
    @Override
    public String storeOTP(String firstName, String lastName, String email, String OTP) {
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE_OTP, Statement.RETURN_GENERATED_KEYS); //email is the key
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, email);
                ps.setString(4, OTP);

                return ps;
            }, keyHolder);
            return (String) keyHolder.getKeys().get("EMAIL");
        } catch (Exception e) {
            throw new MTAuthException("Error: Unable to store the email verification token");
        }
    }
    //Returns email by OTP
    @Override
    public String findByCode(String OTP_input) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_OTP, String.class, new Object[]{OTP_input});
    }

    @Override
    public void removeByCode(String OTP_input) {
        this.jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_DELETE_BY_OTP);
            ps.setString(1, OTP_input);

            return ps;
        });
    }

    @Override
    public void removeByEmail(String email) {
        this.jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_DELETE_BY_EMAIL);
            ps.setString(1, email);
            return ps;
        });
    }

    @Override //MAKE SURE TO CHECK IF IT EXISTS ALREADY, IF SO THEN REMOVE IT THEN ADD NEW ONE.. DO THIS IN SERVICE...
    public Integer findVerifRecordByEmail(String email) {
        return jdbcTemplate.queryForObject(SQL_OTP_COUNT_BY_EMAIL, Integer.class, new Object[]{email});
    }

    @Override
    public Boolean verifyByToken(String token, String email) {
        return (jdbcTemplate.queryForObject(SQL_UU_FIND_BY_EMAIL_AND_TOKEN, Integer.class, new Object[]{token, email}) == 1);
    }

    //updates the password
    private String updatePassword(String email, String newPassword) {
        //Change Password and return true
        String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(10));
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_PASSWORD, Statement.RETURN_GENERATED_KEYS); //here newly generated key will be USER_ID
                ps.setString(1, hashedNewPassword);
                ps.setString(2, email);

                return ps;
            }, keyHolder);
            return (String) keyHolder.getKeys().get("FIRST_NAME");

        } catch (Exception e) {
            throw new MTAuthException("Error: Unable to update the password");
        }
    }

    //record turns into an object of user type
    private RowMapper<User> userRowMapper = ((rs, rowNum) -> {
        return new User(rs.getInt("USER_ID"),
                rs.getString("FIRST_NAME"),
                rs.getString("LAST_NAME"),
                rs.getString("EMAIL"),
                rs.getString("PASSWORD"),
                rs.getString("SECURITY_ANSWER"));
    });

    //record turns into an object of unverifieduser type
    private RowMapper<UnverifiedUser> unverifiedUserRowMapper = ((rs, rowNum) -> {
        return new UnverifiedUser(rs.getString("FIRST_NAME"),
                rs.getString("LAST_NAME"),
                rs.getString("EMAIL"),
                rs.getString("OTP"));
    });
}
