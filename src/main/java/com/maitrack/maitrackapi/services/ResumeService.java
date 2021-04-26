package com.maitrack.maitrackapi.services;

import com.maitrack.maitrackapi.domain.Resume;
import com.maitrack.maitrackapi.exceptions.MTBadRequestException;
import com.maitrack.maitrackapi.exceptions.MTResourceNotFoundException;

import java.util.List;

public interface ResumeService {
    List<Resume> fetchAllResumes(Integer userId);

    Resume fetchResumeById(Integer userId, String resumeName) throws MTResourceNotFoundException;

    Resume addResume(Integer userId, String resumeName) throws MTBadRequestException;

    //Cascade to jobs as well
    void removeResumeWithAllJobs(Integer userId, Integer resumeName) throws MTResourceNotFoundException;

}
