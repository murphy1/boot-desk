package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.model.ServiceRequest;
import com.murphy1.serviced.serviced.model.Status;
import org.springframework.stereotype.Service;
import com.murphy1.serviced.serviced.repositories.ServiceRequestRepository;
import com.murphy1.serviced.serviced.services.ServiceRequestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Override
    public ServiceRequest save(ServiceRequest serviceRequest) {

        if (serviceRequest.getId() == null){
            serviceRequest.setStatus(Status.NEW);
        }

        return serviceRequestRepository.save(serviceRequest);
    }

    @Override
    public ServiceRequest findById(Long id) {

        Optional<ServiceRequest> serviceRequest = serviceRequestRepository.findById(id);

        if (serviceRequest.isEmpty()){
            throw new RuntimeException("Service Request does not exist!");
        }

        return serviceRequest.get();
    }

    @Override
    public void deleteServiceRequest(Long id) {
        serviceRequestRepository.deleteById(id);
    }
}
