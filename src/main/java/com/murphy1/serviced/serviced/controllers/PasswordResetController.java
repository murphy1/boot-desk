package com.murphy1.serviced.serviced.controllers;

import com.murphy1.serviced.serviced.model.ResetToken;
import com.murphy1.serviced.serviced.model.User;
import com.murphy1.serviced.serviced.services.MailService;
import com.murphy1.serviced.serviced.services.ResetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PasswordResetController {

    private MailService mailService;
    private ResetService resetService;

    public PasswordResetController(MailService mailService, ResetService resetService) {
        this.mailService = mailService;
        this.resetService = resetService;
    }

    @GetMapping("/reset")
    public String reset(Model model){
        model.addAttribute("user", new User());

        return "reset_password.html";
    }

    @PostMapping("submit_reset")
    public String submitReset(@ModelAttribute User user){
        ResetToken token = resetService.generateToken(user.getEmail());
        mailService.forgotPassword(user, token);

        return "redirect:/index";
    }

    @GetMapping("/process/{token}")
    public String processReset(@PathVariable String token, Model model){
        User user = resetService.processToken(token);
        model.addAttribute("user", user);

        return "forms/forgot_password.html";
    }

}
