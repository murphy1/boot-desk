package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.model.ServiceRequest;
import org.springframework.stereotype.Service;
import com.murphy1.serviced.serviced.repositories.ServiceRequestRepository;
import com.murphy1.serviced.serviced.services.ServiceRequestService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceRequestServiceImpl implements ServiceRequestService {

    private ServiceRequestRepository serviceRequestRepository;

    public ServiceRequestServiceImpl(ServiceRequestRepository serviceRequestRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
    }


    @Override
    public List<ServiceRequest> getAllServiceRequests() {

        List<ServiceRequest> serviceRequests = new ArrayList<>();

        serviceRequestRepository.findAll().iterator().forEachRemaining(serviceRequests :: add);

        return serviceRequests;
    }
}
