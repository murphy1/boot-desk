package com.murphy1.serviced.serviced.controllers;

import com.murphy1.serviced.serviced.services.IssueService;
import com.murphy1.serviced.serviced.services.ServiceRequestService;
import com.murphy1.serviced.serviced.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    private IssueService issueService;
    private ServiceRequestService serviceRequestService;
    private UserService userService;

    public ProfileController(IssueService issueService, ServiceRequestService serviceRequestService, UserService userService) {
        this.issueService = issueService;
        this.serviceRequestService = serviceRequestService;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String loadProfile(Model model){
        //Check role. If user is admin they will see the Analytics option in Navbar
        String role = userService.getRole(userService.getCurrentUserName());
        model.addAttribute("role", role);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String current_username = ((UserDetails)principal).getUsername();

        model.addAttribute("issues", issueService.findIssueByUser(current_username));
        model.addAttribute("service_requests", serviceRequestService.findRequestByUser(current_username));
        model.addAttribute("assigned_issues", issueService.findAssignedIssuesByUser(current_username));
        model.addAttribute("assigned_service_requests", serviceRequestService.findRequestByAssignedUser(current_username));

        return "profile.html";
    }

}
