package com.maitrack.maitrackapi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//Just an example
@Controller
public class ExampleController {
    @RequestMapping("/email_template")
    public String example(){
        return "email_template";
    }
}
