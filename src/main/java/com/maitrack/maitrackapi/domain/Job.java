package com.maitrack.maitrackapi.domain;

import java.time.LocalDate;

public class Job {
    private Integer jobId;
    private Integer userId;
    private Integer resumeId;
    private LocalDate applicationDate;
    private String jobTitle;
    private String jobType;
    private String jobDescription;
    private String jobCity;
    private String jobState;
    private String jobCountry;
    private String companyName;
    private String jobBoard;
    private String interviewStatus;

    public Job(Integer jobId, Integer userId, Integer resumeId, LocalDate applicationDate, String jobTitle, String jobType, String jobDescription, String jobCity, String jobState, String jobCountry, String companyName, String jobBoard, String interviewStatus) {
        this.jobId = jobId;
        this.userId = userId;
        this.resumeId = resumeId;
        this.applicationDate = applicationDate;
        this.jobTitle = jobTitle;
        this.jobType = jobType;
        this.jobDescription = jobDescription;
        this.jobCity = jobCity;
        this.jobState = jobState;
        this.jobCountry = jobCountry;
        this.companyName = companyName;
        this.jobBoard = jobBoard;
        this.interviewStatus = interviewStatus;
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

    public Integer getResumeId() {
        return resumeId;
    }

    public void setResumeId(Integer resumeId) {
        this.resumeId = resumeId;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobCity() {
        return jobCity;
    }

    public void setJobCity(String jobCity) {
        this.jobCity = jobCity;
    }

    public String getJobState() {
        return jobState;
    }

    public void setJobState(String jobState) {
        this.jobState = jobState;
    }

    public String getJobCountry() {
        return jobCountry;
    }

    public void setJobCountry(String jobCountry) {
        this.jobCountry = jobCountry;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobBoard() {
        return jobBoard;
    }

    public void setJobBoard(String jobBoard) {
        this.jobBoard = jobBoard;
    }

    public String getInterviewStatus() {
        return interviewStatus;
    }

    public void setInterviewStatus(String interviewStatus) {
        this.interviewStatus = interviewStatus;
    }

}
