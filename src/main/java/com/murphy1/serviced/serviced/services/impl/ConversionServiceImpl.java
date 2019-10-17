package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.model.Issue;
import com.murphy1.serviced.serviced.model.ServiceRequest;
import com.murphy1.serviced.serviced.model.Status;
import com.murphy1.serviced.serviced.repositories.IssueRepository;
import com.murphy1.serviced.serviced.repositories.ServiceRequestRepository;
import com.murphy1.serviced.serviced.services.ConversionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConversionServiceImpl implements ConversionService {

    private IssueRepository issueRepository;
    private ServiceRequestRepository serviceRequestRepository;

    public ConversionServiceImpl(IssueRepository issueRepository, ServiceRequestRepository serviceRequestRepository) {
        this.issueRepository = issueRepository;
        this.serviceRequestRepository = serviceRequestRepository;
    }

    @Override
    public ServiceRequest convertIssueToServiceRequest(Issue issue) {
        ServiceRequest serviceRequest = new ServiceRequest();

        serviceRequest.setName(issue.getName());
        serviceRequest.setDescription(issue.getDescription());
        serviceRequest.setPriority(issue.getPriority());
        serviceRequest.setDueDate(issue.getDueDate());
        serviceRequest.setStatus(issue.getStatus());
        serviceRequest.setLabel(issue.getLabel());
        if (!issue.getMessages().equals("")){
            serviceRequest.setMessages(issue.getMessages()+"\n\nConverted from Issue "+issue.getId().toString());
        }
        if (!issue.getCreator().equals("")){
            serviceRequest.setCreator(issue.getCreator());
        }
        if (!issue.getAssignedTo().equals("")){
            serviceRequest.setAssignedTo(issue.getAssignedTo());
        }

        serviceRequestRepository.save(serviceRequest);


        String convertComplete = "Converted To Service Request "+serviceRequest.getId().toString();
        String oldMessages = issue.getMessages();
        if (oldMessages == null){
            issue.setMessages(convertComplete);
        }
        else {
            issue.setMessages(oldMessages + "\n\n" + convertComplete);
        }

        issue.setStatus(Status.SOLVED);

        issueRepository.save(issue);

        return serviceRequest;
    }

    @Override
    public Issue convertServiceRequestToIssue(ServiceRequest serviceRequest) {
        Issue issue = new Issue();

        issue.setName(serviceRequest.getName());
        issue.setDescription(serviceRequest.getDescription());
        issue.setPriority(serviceRequest.getPriority());
        issue.setDueDate(serviceRequest.getDueDate());
        issue.setStatus(serviceRequest.getStatus());
        issue.setLabel(serviceRequest.getLabel());
        if (!serviceRequest.getMessages().equals("")){
            issue.setMessages(serviceRequest.getMessages()+"\n\nConverted from Service Request "+serviceRequest.getId().toString());
        }
        if (!serviceRequest.getCreator().equals("")){
            issue.setCreator(serviceRequest.getCreator());
        }
        if (!serviceRequest.getAssignedTo().equals("")){
            issue.setAssignedTo(serviceRequest.getAssignedTo());
        }

        issueRepository.save(issue);

        String convertComplete = "Converted To Issue "+issue.getId().toString();
        String oldMessages = serviceRequest.getMessages();
        if (oldMessages == null){
            serviceRequest.setMessages(convertComplete);
        }
        else {
            serviceRequest.setMessages(oldMessages + "\n\n" + convertComplete);
        }

        serviceRequest.setStatus(Status.SOLVED);

        serviceRequestRepository.save(serviceRequest);

        return issue;
    }
}
