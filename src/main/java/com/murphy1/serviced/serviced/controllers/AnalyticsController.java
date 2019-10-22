package com.murphy1.serviced.serviced.controllers;

import com.murphy1.serviced.serviced.model.Issue;
import com.murphy1.serviced.serviced.model.User;
import com.murphy1.serviced.serviced.services.AnalyticsService;
import com.murphy1.serviced.serviced.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class AnalyticsController {

    private AnalyticsService analyticsService;
    private UserService userService;

    public AnalyticsController(AnalyticsService analyticsService, UserService userService) {
        this.analyticsService = analyticsService;
        this.userService = userService;
    }

    @GetMapping("/analytics")
    public String openAnalytics(Model model){
        //Check role. If user is admin they will see the Analytics option in Navbar
        String role = userService.getRole(userService.getCurrentUserName());
        model.addAttribute("role", role);

        model.addAttribute("issue", new Issue());

        model.addAttribute("current_open_tickets", analyticsService.getAllOpenTickets());
        model.addAttribute("overdue_tickets", analyticsService.overDueTickets());
        model.addAttribute("overall_users", analyticsService.allUsers());
        model.addAttribute("admins", analyticsService.allAdmins());
        model.addAttribute("agents", analyticsService.allAgents());
        model.addAttribute("endusers", analyticsService.allEndUsers());

        return "/analytics.html";
    }

    @GetMapping("/analytics/user")
    public String getUserAnalytics(){

        return "analytics/user_analytics.html";
    }

    @PostMapping("/user_analytics")
    public String getUserForAnalytics(@ModelAttribute("users") User user, Model model){
        //Check role. If user is admin they will see the Analytics option in Navbar
        String role = userService.getRole(userService.getCurrentUserName());
        model.addAttribute("role", role);

        User user1 = userService.findUserByUsername(user.getUsername());

        model.addAttribute("user", user1.getUsername());
        model.addAttribute("currentAssignedTickets", analyticsService.openTicketsByUsername(user1.getUsername()));
        model.addAttribute("ticketsClosed", analyticsService.ticketsClosedByUsername(user1.getUsername()));

        return "analytics/user_analytics.html";
    }

    @PostMapping("label_analytics")
    public String getLabelAnalytics(@ModelAttribute("issue") Issue issue, Model model){
        //Check role. If user is admin they will see the Analytics option in Navbar
        String role = userService.getRole(userService.getCurrentUserName());
        model.addAttribute("role", role);

        String lbl = issue.getLabel().toString();

        model.addAttribute("label", analyticsService.ticketsByLabel(lbl));

        return "analytics/labelanalytics.html";
    }

}
