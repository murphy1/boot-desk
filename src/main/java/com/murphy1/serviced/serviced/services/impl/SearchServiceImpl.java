package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.exceptions.BadRequestException;
import com.murphy1.serviced.serviced.model.Issue;
import com.murphy1.serviced.serviced.model.ServiceRequest;
import com.murphy1.serviced.serviced.services.IssueService;
import com.murphy1.serviced.serviced.services.SearchService;
import com.murphy1.serviced.serviced.services.ServiceRequestService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    private IssueService issueService;
    private ServiceRequestService serviceRequestService;

    public SearchServiceImpl(IssueService issueService, ServiceRequestService serviceRequestService) {
        this.issueService = issueService;
        this.serviceRequestService = serviceRequestService;
    }

    @Override
    public List<Issue> issueResult(String object, String query) {
        List<Issue> results = new ArrayList<>();
        List<Issue> issues = issueService.getAllIssues();

        switch (object) {
            case "ID":
                try {
                    Long id = Long.valueOf(query);
                    for (Issue issue : issues) {
                        if (issue.getId().equals(id)) {
                            results.add(issue);
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                    throw new BadRequestException("When searching for an ID please enter a number!");
                }
                break;
            case "NAME":
                for (Issue issue : issues) {
                    if (issue.getName().toLowerCase().contains(query.toLowerCase()) || issue.getName().toLowerCase().equalsIgnoreCase(query)) {
                        results.add(issue);
                    }
                }
                break;
            case "DESCRIPTION":
                for (Issue issue : issues) {
                    if (issue.getDescription().toLowerCase().contains(query.toLowerCase()) || issue.getDescription().toLowerCase().equalsIgnoreCase(query)) {
                        results.add(issue);
                    }
                }
                break;
            case "PRIORITY":
                for (Issue issue : issues) {
                    if (issue.getPriority() == null){
                        continue;
                    }
                    if (issue.getPriority().toString().equalsIgnoreCase(query)) {
                        results.add(issue);
                    }
                }
                break;
            case "STATUS":
                for (Issue issue : issues) {
                    if (issue.getStatus() == null){
                        continue;
                    }
                    if (issue.getStatus().toString().equalsIgnoreCase(query)) {
                        results.add(issue);
                    }
                }
                break;
            case "LABEL":
                for (Issue issue : issues) {
                    if (issue.getLabel() == null){
                        continue;
                    }
                    if (issue.getLabel().toString().equalsIgnoreCase(query)) {
                        results.add(issue);
                    }
                }
                break;
            case "CREATOR":
                for (Issue issue : issues) {
                    if (issue.getCreator().equalsIgnoreCase(query)) {
                        results.add(issue);
                    }
                }
                break;
            case "ASSIGNED_TO":
                for (Issue issue : issues) {
                    if (issue.getAssignedTo() == null){
                        continue;
                    }
                    if (issue.getAssignedTo().equalsIgnoreCase(query)) {
                        results.add(issue);
                    }
                }
                break;
        }

        return results;
    }

    @Override
    public List<ServiceRequest> serviceRequestResult(String object, String query) {
        List<ServiceRequest> results = new ArrayList<>();
        List<ServiceRequest> serviceRequests = serviceRequestService.getAllServiceRequests();

        switch (object) {
            case "ID":
                try {
                    Long id = Long.valueOf(query);
                    for (ServiceRequest serviceRequest : serviceRequests) {
                        if (serviceRequest.getId().equals(id)) {
                            results.add(serviceRequest);
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                    throw new BadRequestException("When searching for an ID please enter a number!");
                }
                break;
            case "NAME":
                for (ServiceRequest serviceRequest : serviceRequests) {
                    if (serviceRequest.getName().toLowerCase().contains(query.toLowerCase()) || serviceRequest.getName().toLowerCase().equalsIgnoreCase(query)) {
                        results.add(serviceRequest);
                    }
                }
                break;
            case "DESCRIPTION":
                for (ServiceRequest serviceRequest : serviceRequests) {
                    if (serviceRequest.getDescription().toLowerCase().contains(query.toLowerCase()) || serviceRequest.getDescription().toLowerCase().equalsIgnoreCase(query)) {
                        results.add(serviceRequest);
                    }
                }
                break;
            case "PRIORITY":
                for (ServiceRequest serviceRequest : serviceRequests) {
                    if (serviceRequest.getPriority() == null){
                        continue;
                    }
                    if (serviceRequest.getPriority().toString().equalsIgnoreCase(query)) {
                        results.add(serviceRequest);
                    }
                }
                break;
            case "STATUS":
                for (ServiceRequest serviceRequest : serviceRequests) {
                    if (serviceRequest.getStatus() == null){
                        continue;
                    }
                    if (serviceRequest.getStatus().toString().equalsIgnoreCase(query)) {
                        results.add(serviceRequest);
                    }
                }
                break;
            case "LABEL":
                for (ServiceRequest serviceRequest : serviceRequests) {
                    if (serviceRequest.getLabel() == null){
                        continue;
                    }
                    if (serviceRequest.getLabel().toString().equalsIgnoreCase(query)) {
                        results.add(serviceRequest);
                    }
                }
                break;
            case "CREATOR":
                for (ServiceRequest serviceRequest : serviceRequests) {
                    if (serviceRequest.getCreator().equalsIgnoreCase(query)) {
                        results.add(serviceRequest);
                    }
                }
                break;
            case "ASSIGNED_TO":
                for (ServiceRequest serviceRequest : serviceRequests) {
                    if (serviceRequest.getAssignedTo() == null){
                        continue;
                    }
                    if (serviceRequest.getAssignedTo().equalsIgnoreCase(query)) {
                        results.add(serviceRequest);
                    }
                }
                break;
        }

        return results;
    }
}
