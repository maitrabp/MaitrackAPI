package com.maitrack.maitrackapi.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/jobs")
public class JobsResource {

    @GetMapping("")
    public String getAllJobs(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        return "Authenticated! Email: " + email;
    }
}
