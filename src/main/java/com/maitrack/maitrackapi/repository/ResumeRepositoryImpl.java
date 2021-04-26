package com.maitrack.maitrackapi.repository;

import com.maitrack.maitrackapi.domain.Resume;
import com.maitrack.maitrackapi.exceptions.MTBadRequestException;
import com.maitrack.maitrackapi.exceptions.MTResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ResumeRepositoryImpl implements ResumeRepository{

    private static final String SQL_CREATE = "INSERT INTO MT_RESUMES (USER_ID, RESUME_NAME) VALUES(?, ?)";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Resume> findAll(Integer userId) throws MTResourceNotFoundException {
        return null;
    }

    @Override
    public Resume findById(Integer userId, String resumeName) {
        return null;
    }

    @Override
    public Resume create(Integer userId, String resumeName) throws MTBadRequestException {
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setString(2, resumeName);
                return ps;
            }, keyHolder);
            return new Resume((Integer)keyHolder.getKeys().get("USER_ID"), (String)keyHolder.getKeys().get("RESUME_NAME"));
        }catch(Exception e) {
            throw new MTBadRequestException("Invalid Request");
        }
    }

    @Override
    public void removeById(Integer userId, String resumeName) throws MTResourceNotFoundException {

    }
}
