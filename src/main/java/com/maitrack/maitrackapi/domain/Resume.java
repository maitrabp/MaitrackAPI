package com.maitrack.maitrackapi.domain;

public class Resume {
    private Integer userId;
    private String resumeName;

    public Resume(Integer userId, String resumeName) {
        this.userId = userId;
        this.resumeName = resumeName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getResumeName() {
        return resumeName;
    }

    public void setResumeName(String resumeName) {
        this.resumeName = resumeName;
    }
}
