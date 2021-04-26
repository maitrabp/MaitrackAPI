package com.maitrack.maitrackapi.domain;
import java.time.LocalDateTime;


public class Interview {
    private Integer jobId;
    private Integer userId;
    private LocalDateTime interviewTime;

    public Interview(Integer jobId, Integer userId, LocalDateTime interviewTime) {
        this.jobId = jobId;
        this.userId = userId;
        this.interviewTime = interviewTime;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getInterviewTime() {
        return interviewTime;
    }

    public void setInterviewTime(LocalDateTime interviewTime) {
        this.interviewTime = interviewTime;
    }
}
