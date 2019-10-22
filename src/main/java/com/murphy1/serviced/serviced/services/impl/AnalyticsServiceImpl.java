package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.exceptions.BadRequestException;
import com.murphy1.serviced.serviced.model.Label;
import com.murphy1.serviced.serviced.services.AnalyticsService;
import com.murphy1.serviced.serviced.services.IssueService;
import com.murphy1.serviced.serviced.services.ServiceRequestService;
import com.murphy1.serviced.serviced.services.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private IssueService issueService;
    private ServiceRequestService serviceRequestService;
    private UserService userService;

    public AnalyticsServiceImpl(IssueService issueService, ServiceRequestService serviceRequestService, UserService userService) {
        this.issueService = issueService;
        this.serviceRequestService = serviceRequestService;
        this.userService = userService;
    }

    // General Stats

    @Override
    public Long getAllOpenTickets() {
        Long issues = issueService.getAllIssues().stream()
                .filter(issue -> !issue.getStatus().toString().equals("SOLVED"))
                .count();

        Long serviceRequests = serviceRequestService.getAllServiceRequests().stream()
                .filter(serviceRequest -> !serviceRequest.getStatus().toString().equals("SOLVED"))
                .count();
        return issues + serviceRequests;
    }

    @Override
    public Long overDueTickets() {
        Long issues = issueService.getAllIssues().stream()
                .filter(issue -> issue.getDueDate().isBefore(LocalDate.now()))
                .count();

        Long serviceRequests = serviceRequestService.getAllServiceRequests().stream()
                .filter(serviceRequest -> serviceRequest.getDueDate().isBefore(LocalDate.now()))
                .count();
        return issues + serviceRequests;
    }

    // User Stats

    @Override
    public Long openTicketsByUsername(String username) {
        Long issues = issueService.getAllIssues().stream()
                .filter(issue -> !issue.getStatus().toString().equals("SOLVED"))
                .filter(issue -> issue.getAssignedTo().equals(username))
                .count();
        Long serviceRequest = serviceRequestService.getAllServiceRequests().stream()
                .filter(serviceRequest1 -> !serviceRequest1.getStatus().toString().equals("SOLVED"))
                .filter(serviceRequest1 -> serviceRequest1.getAssignedTo().equals(username))
                .count();
        return issues + serviceRequest;
    }

    @Override
    public Long ticketsClosedByUsername(String username) {
        Long issues = issueService.getAllIssues().stream()
                .filter(issue -> issue.getStatus().toString().equals("SOLVED"))
                .filter(issue -> issue.getAssignedTo().equals(username))
                .count();
        Long serviceRequest = serviceRequestService.getAllServiceRequests().stream()
                .filter(serviceRequest1 -> serviceRequest1.getStatus().toString().equals("SOLVED"))
                .filter(serviceRequest1 -> serviceRequest1.getAssignedTo().equals(username))
                .count();
        return issues + serviceRequest;
    }

    @Override
    public Long allUsers() {
        return (long) userService.getAllUsers().size();
    }

    @Override
    public Long allAdmins() {
        return userService.getAllUsers().stream()
                .filter(user -> user.getRoles().equals("ADMIN"))
                .count();
    }

    @Override
    public Long allAgents() {
        return userService.getAllUsers().stream()
                .filter(user -> user.getRoles().equals("AGENT"))
                .count();
    }

    @Override
    public Long allEndUsers() {
        return userService.getAllUsers().stream()
                .filter(user -> user.getRoles().equals("END_USER"))
                .count();
    }

    @Override
    public Long ticketsByLabel(String label) {

        Label[] labels = Label.values();
        Optional<Label> lblOptional = Arrays.stream(labels)
                .filter(label1 -> label1.toString().equalsIgnoreCase(label))
                .findFirst();

        if (!lblOptional.isPresent()){
            throw new BadRequestException("That label does not exist!");
        }

        Long issues = issueService.getAllIssues().stream()
                .filter(issue -> issue.getLabel().toString().equals(label))
                .count();
        Long serviceRequests = serviceRequestService.getAllServiceRequests().stream()
                .filter(serviceRequest -> serviceRequest.getLabel().toString().equals(label))
                .count();
        return issues + serviceRequests;
    }
}
