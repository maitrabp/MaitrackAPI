package com.maitrack.maitrackapi.services;

import com.maitrack.maitrackapi.domain.Resume;
import com.maitrack.maitrackapi.exceptions.MTBadRequestException;
import com.maitrack.maitrackapi.exceptions.MTResourceNotFoundException;
import com.maitrack.maitrackapi.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional //For DB transactions using methods, whole trans. rolled out on runtime exception..
public class ResumeServiceImpl implements ResumeService{

    @Autowired
    ResumeRepository resumeRepository;

    @Override
    public List<Resume> fetchAllResumes(Integer userId) {
        return null;
    }

    @Override
    public Resume fetchResumeById(Integer userId, String resumeName) throws MTResourceNotFoundException {
        return null;
    }

    @Override
    public Resume addResume(Integer userId, String resumeName) throws MTBadRequestException {
        Resume res = resumeRepository.create(userId, resumeName);
        return resumeRepository.findById(res.getUserId(), res.getResumeName());
    }

    @Override
    public void removeResumeWithAllJobs(Integer userId, Integer resumeName) throws MTResourceNotFoundException {

    }
}
