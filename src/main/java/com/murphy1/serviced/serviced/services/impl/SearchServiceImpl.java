package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.exceptions.BadRequestException;
import com.murphy1.serviced.serviced.model.Issue;
import com.murphy1.serviced.serviced.model.ServiceRequest;
import com.murphy1.serviced.serviced.model.Ticket;
import com.murphy1.serviced.serviced.services.IssueService;
import com.murphy1.serviced.serviced.services.SearchService;
import com.murphy1.serviced.serviced.services.ServiceRequestService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    private IssueService issueService;
    private ServiceRequestService serviceRequestService;

    public SearchServiceImpl(IssueService issueService, ServiceRequestService serviceRequestService) {
        this.issueService = issueService;
        this.serviceRequestService = serviceRequestService;
    }

    @Override
    public List<Ticket> issueResult(String object, String query) {
        List<Issue> issues = issueService.getAllIssues();

        List<Ticket> issuesAsTickets = issues
                .stream()
                .map(e -> (Ticket) e)
                .collect(Collectors.toList());

        return search(issuesAsTickets , object, query);
    }

    @Override
    public List<Ticket> issueResult(String object, String query, String object1, String query1) {
        List<Ticket> firstResult = issueResult(object, query);
        return search(firstResult, object1, query1);
    }

    @Override
    public List<Ticket> serviceRequestResult(String object, String query) {
        List<ServiceRequest> serviceRequests = serviceRequestService.getAllServiceRequests();

        List<Ticket> serviceRequestsAsTickets = serviceRequests
                .stream()
                .map(e -> (Ticket) e)
                .collect(Collectors.toList());

        return search(serviceRequestsAsTickets , object, query);
    }

    @Override
    public List<Ticket> serviceRequestResult(String object, String query, String object1, String query1) {
        List<Ticket> firstSearch = serviceRequestResult(object, query);
        return search(firstSearch, object1, query1);
    }

    @Override
    public List<Ticket> search(List<Ticket> listToSearch, String object, String query) {
        List<Ticket> results = new ArrayList<>();

        switch (object) {
            case "ID":
                try {
                    Long id = Long.valueOf(query);
                    for (Ticket ticket : listToSearch) {
                        if (ticket.getId().equals(id)) {
                            results.add(ticket);
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                    throw new BadRequestException("When searching for an ID please enter a number!");
                }
                break;
            case "NAME":
                for (Ticket ticket : listToSearch) {
                    if (ticket.getName().toLowerCase().contains(query.toLowerCase()) || ticket.getName().toLowerCase().equalsIgnoreCase(query)) {
                        results.add(ticket);
                    }
                }
                break;
            case "DESCRIPTION":
                for (Ticket ticket : listToSearch) {
                    if (ticket.getDescription().toLowerCase().contains(query.toLowerCase()) || ticket.getDescription().toLowerCase().equalsIgnoreCase(query)) {
                        results.add(ticket);
                    }
                }
                break;
            case "PRIORITY":
                for (Ticket ticket : listToSearch) {
                    if (ticket.getPriority() == null){
                        continue;
                    }
                    if (ticket.getPriority().toString().equalsIgnoreCase(query)) {
                        results.add(ticket);
                    }
                }
                break;
            case "STATUS":
                for (Ticket ticket : listToSearch) {
                    if (ticket.getStatus() == null){
                        continue;
                    }
                    if (ticket.getStatus().toString().equalsIgnoreCase(query)) {
                        results.add(ticket);
                    }
                }
                break;
            case "LABEL":
                for (Ticket ticket : listToSearch) {
                    if (ticket.getLabel() == null){
                        continue;
                    }
                    if (ticket.getLabel().toString().equalsIgnoreCase(query)) {
                        results.add(ticket);
                    }
                }
                break;
            case "CREATOR":
                for (Ticket ticket : listToSearch) {
                    if (ticket.getCreator().equalsIgnoreCase(query)) {
                        results.add(ticket);
                    }
                }
                break;
            case "ASSIGNED_TO":
                for (Ticket ticket : listToSearch) {
                    if (ticket.getAssignedTo() == null){
                        continue;
                    }
                    if (ticket.getAssignedTo().equalsIgnoreCase(query)) {
                        results.add(ticket);
                    }
                }
                break;
        }

        return results;
    }
}
