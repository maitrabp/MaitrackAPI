package com.maitrack.maitrackapi.repository;

import com.maitrack.maitrackapi.domain.Resume;
import com.maitrack.maitrackapi.exceptions.MTBadRequestException;
import com.maitrack.maitrackapi.exceptions.MTResourceNotFoundException;

import java.util.List;

public interface ResumeRepository {

    List<Resume> findAll(Integer userId) throws MTResourceNotFoundException;

    Resume findById(Integer  userId, String resumeName);

    Resume create(Integer userId, String resumeName) throws MTBadRequestException;

    //Cascade to jobs as well
    void removeById(Integer userId, String resumeName) throws MTResourceNotFoundException;

}
