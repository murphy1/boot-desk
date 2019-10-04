package com.murphy1.serviced.serviced.services;

import com.murphy1.serviced.serviced.model.ServiceRequest;

import java.util.List;

public interface ServiceRequestService {
    List<ServiceRequest> getAllServiceRequests();
    ServiceRequest save(ServiceRequest serviceRequest);
    ServiceRequest findById(Long id);
    void deleteServiceRequest(Long id);
}
