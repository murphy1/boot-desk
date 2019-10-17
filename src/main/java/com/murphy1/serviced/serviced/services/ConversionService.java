package com.murphy1.serviced.serviced.services;

import com.murphy1.serviced.serviced.model.Issue;
import com.murphy1.serviced.serviced.model.ServiceRequest;

public interface ConversionService {
    ServiceRequest convertIssueToServiceRequest(Issue issue);
    Issue convertServiceRequestToIssue(ServiceRequest serviceRequest);
}
