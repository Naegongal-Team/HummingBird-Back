package com.negongal.hummingbird;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    @RequestMapping("/info")
    public String projectInfo() {
        return "Project name is HummingBird.";
    }
}

