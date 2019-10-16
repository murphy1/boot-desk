package com.murphy1.serviced.serviced.controllers;

import com.murphy1.serviced.serviced.exceptions.ForbiddenException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping({"/", "/index", "/home"})
    public String homepage(){

        return "index.html";
    }

    // throw error if user tries to access a forbidden page
    @GetMapping("/exceptions/403error")
    public String forbiddenError(){
        throw new ForbiddenException("You do not have access to this page!");
    }

}
