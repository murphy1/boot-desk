package com.murphy1.serviced.serviced.rest;

import com.murphy1.serviced.serviced.model.Team;
import com.murphy1.serviced.serviced.model.Ticket;
import com.murphy1.serviced.serviced.model.User;
import com.murphy1.serviced.serviced.services.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RestAnalyticsController {

    private TeamService teamService;
    private AnalyticsService analyticsService;
    private IssueService issueService;
    private ServiceRequestService serviceRequestService;
    private UserService userService;

    public RestAnalyticsController(TeamService teamService, AnalyticsService analyticsService, IssueService issueService, ServiceRequestService serviceRequestService, UserService userService) {
        this.teamService = teamService;
        this.analyticsService = analyticsService;
        this.issueService = issueService;
        this.serviceRequestService = serviceRequestService;
        this.userService = userService;
    }

    @GetMapping("/api/v1/analytics/teams")
    public List<Team> getAllTeams(){
        return teamService.getAllTeams();
    }

    @GetMapping("/api/v1/analytics/teams/{id}")
    public Team getTeamById(@PathVariable String id){
        return teamService.getAllTeams().stream()
                .filter(team -> team.getId().equals(Long.valueOf(id)))
                .findFirst()
                .get();
    }

    @GetMapping("/api/v1/analytics/overdue_tickets")
    public List<Ticket> getOverdueTickets(){
        List<Ticket> issues = issueService.getAllIssues().stream()
                .filter(issue -> issue.getDueDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
        serviceRequestService.getAllServiceRequests().stream()
                .filter(serviceRequest -> serviceRequest.getDueDate().isBefore(LocalDate.now()))
                .map(issues::add);
        return issues;
    }

    @GetMapping("/api/v1/analytics/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

}
