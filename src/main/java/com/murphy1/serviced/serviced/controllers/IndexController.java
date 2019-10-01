package com.murphy1.serviced.serviced.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping({"/", "/index", "/home"})
    public String homepage(){

        return "index.html";
    }

}
