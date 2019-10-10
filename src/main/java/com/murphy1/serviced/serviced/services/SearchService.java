package com.murphy1.serviced.serviced.services;

import com.murphy1.serviced.serviced.model.Issue;
import com.murphy1.serviced.serviced.model.ServiceRequest;

import java.util.List;

public interface SearchService {
    List<Issue> issueResult(String object, String query);
    List<ServiceRequest> serviceRequestResult(String object, String query);
}
