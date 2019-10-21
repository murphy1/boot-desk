package com.murphy1.serviced.serviced.controllers;

import com.murphy1.serviced.serviced.exceptions.ForbiddenException;
import com.murphy1.serviced.serviced.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private UserService userService;

    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", "/index", "/home"})
    public String homepage(Model model){
        String role = userService.getRole(userService.getCurrentUserName());
        model.addAttribute("role", role);

        return "index.html";
    }

    // throw error if user tries to access a forbidden page
    @GetMapping("/exceptions/403error")
    public String forbiddenError(){
        throw new ForbiddenException("You do not have access to this page!");
    }

}
