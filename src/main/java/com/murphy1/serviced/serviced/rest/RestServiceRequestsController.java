package com.murphy1.serviced.serviced.rest;

import com.murphy1.serviced.serviced.model.ServiceRequest;
import com.murphy1.serviced.serviced.services.ServiceRequestService;
import org.springframework.web.bind.annotation.RestController;
import com.murphy1.serviced.serviced.model.Status;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RestServiceRequestsController {

    private ServiceRequestService serviceRequestService;

    public RestServiceRequestsController(ServiceRequestService serviceRequestService) {
        this.serviceRequestService = serviceRequestService;
    }

    @GetMapping("/api/v1/service_requests")
    public List<ServiceRequest> getAllServiceRequests(){
        return serviceRequestService.getAllServiceRequests();
    }

    @GetMapping("/api/v1/service_requests/open")
    public List<ServiceRequest> getAllOpenRequests(){
        return serviceRequestService.getAllServiceRequests().stream()
                .filter(serviceRequest -> !serviceRequest.getStatus().equals(Status.SOLVED))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v1/service_requests/closed")
    public List<ServiceRequest> getAllClosedServiceRequests(){
        return serviceRequestService.getAllServiceRequests().stream()
                .filter(serviceRequest -> serviceRequest.getStatus().equals(Status.SOLVED))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v1/service_requests/{id}")
    public ServiceRequest getServiceRequestById(@PathVariable String id){
        return serviceRequestService.findById(Long.valueOf(id));
    }
}
