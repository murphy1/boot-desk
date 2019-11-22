package com.murphy1.serviced.serviced.rest;

import com.murphy1.serviced.serviced.model.Status;
import com.murphy1.serviced.serviced.model.Ticket;
import com.murphy1.serviced.serviced.services.IssueService;
import com.murphy1.serviced.serviced.services.ServiceRequestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RestTicketController {

    private IssueService issueService;
    private ServiceRequestService serviceRequestService;

    public RestTicketController(IssueService issueService, ServiceRequestService serviceRequestService) {
        this.issueService = issueService;
        this.serviceRequestService = serviceRequestService;
    }

    @GetMapping("/api/v1/tickets")
    public List<Ticket> getAllTickets(){
        List<Ticket> tickets = new ArrayList<>();
        tickets.addAll(issueService.getAllIssues());
        tickets.addAll(serviceRequestService.getAllServiceRequests());

        return tickets;
    }

    @GetMapping("/api/v1/tickets/open")
    public List<Ticket> getAllOpenTickets(){
        List<Ticket> results = new ArrayList<>();
        results.addAll(issueService.getAllIssues().stream()
                .filter(issue -> !issue.getStatus().equals(Status.SOLVED))
                .collect(Collectors.toList()));
        results.addAll(serviceRequestService.getAllServiceRequests().stream()
                .filter(serviceRequest -> !serviceRequest.getStatus().equals(Status.SOLVED))
                .collect(Collectors.toList()));

        return results;
    }

    @GetMapping("/api/v1/tickets/closed")
    public List<Ticket> getAllClosedTickets(){
        List<Ticket> results = new ArrayList<>();
        results.addAll(issueService.getAllIssues().stream()
                .filter(issue -> issue.getStatus().equals(Status.SOLVED))
                .collect(Collectors.toList()));
        results.addAll(serviceRequestService.getAllServiceRequests().stream()
                .filter(serviceRequest -> serviceRequest.getStatus().equals(Status.SOLVED))
                .collect(Collectors.toList()));

        return results;
    }

}
