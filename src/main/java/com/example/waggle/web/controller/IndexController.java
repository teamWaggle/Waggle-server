package com.example.waggle.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class IndexController implements ErrorController {
    @GetMapping({"/", "/error"})
    public String index() {
        return "index.html";
    }

}
