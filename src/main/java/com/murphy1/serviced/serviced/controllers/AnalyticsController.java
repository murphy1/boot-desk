package com.murphy1.serviced.serviced.controllers;

import com.murphy1.serviced.serviced.model.Issue;
import com.murphy1.serviced.serviced.model.Team;
import com.murphy1.serviced.serviced.model.User;
import com.murphy1.serviced.serviced.services.AnalyticsService;
import com.murphy1.serviced.serviced.services.TeamService;
import com.murphy1.serviced.serviced.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class AnalyticsController {

    private AnalyticsService analyticsService;
    private UserService userService;
    private TeamService teamService;

    public AnalyticsController(AnalyticsService analyticsService, UserService userService, TeamService teamService) {
        this.analyticsService = analyticsService;
        this.userService = userService;
        this.teamService = teamService;
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

    @GetMapping("analytics/teams")
    public String getTeams(Model model){
        model.addAttribute("teams", analyticsService.getAllTeams());

        return "teams.html";
    }

    @GetMapping("/teams/new")
    public String newTeam(Model model){
        model.addAttribute("team", new Team());

        return "forms/team_form.html";
    }

    @PostMapping("/team/save")
    public String saveTeam(@ModelAttribute("team") Team team){
        teamService.saveTeam(team);

        return "redirect:/analytics/teams";
    }

    @GetMapping("/teams/update/{teamId}")
    public String updateTeam(Model model, @PathVariable String teamId){
        model.addAttribute("team", teamService.updateTeam(Long.valueOf(teamId)));
        model.addAttribute("teamMembers", teamService.updateTeam(Long.valueOf(teamId)).getTeamMembers());

        Long target = teamService.updateTeam(Long.valueOf(teamId)).getTarget();
        Long progressToTarget = teamService.updateTeam(Long.valueOf(teamId)).getProgressToTarget();
        Double progressPercentage = progressToTarget / (Double.valueOf(target) / 100);
        model.addAttribute("progressPercentage", progressPercentage);

        return "forms/team_form.html";
    }

    @GetMapping("/teams/delete/{teamId}")
    public String deleteTeam(@PathVariable String teamId){
        teamService.deleteTeamById(Long.valueOf(teamId));

        return "redirect:/analytics/teams";
    }



}
