package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.model.*;
import com.murphy1.serviced.serviced.repositories.AdminRepository;
import com.murphy1.serviced.serviced.repositories.AgentRepository;
import com.murphy1.serviced.serviced.repositories.EndUserRepository;
import com.murphy1.serviced.serviced.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.murphy1.serviced.serviced.repositories.ServiceRequestRepository;
import com.murphy1.serviced.serviced.services.ServiceRequestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceRequestServiceImpl implements ServiceRequestService {

    private ServiceRequestRepository serviceRequestRepository;
    private AdminRepository adminRepository;
    private AgentRepository agentRepository;
    private EndUserRepository endUserRepository;

    private UserService userService;

    public ServiceRequestServiceImpl(ServiceRequestRepository serviceRequestRepository, AdminRepository adminRepository, AgentRepository agentRepository, EndUserRepository endUserRepository, UserService userService) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.adminRepository = adminRepository;
        this.agentRepository = agentRepository;
        this.endUserRepository = endUserRepository;
        this.userService = userService;
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

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();

            String username = ((UserDetails) principal).getUsername();

            String role = userService.getRole(username);

            List<ServiceRequest> serviceRequests;

            if (role.equals("ADMIN")){
                Optional<Admin> adminOptional = adminRepository.findByUsername(username);
                serviceRequests = adminOptional.get().getServiceRequest();
                serviceRequests.add(serviceRequest);
            }
            else if (role.equals("AGENT")){
                Optional<Agent> agentOptional = agentRepository.findByUsername(username);
                serviceRequests = agentOptional.get().getServiceRequest();
                serviceRequests.add(serviceRequest);
            }
            else if (role.equals("END_USER")){
                Optional<EndUser> endUserOptional = endUserRepository.findByUsername(username);
                serviceRequests = endUserOptional.get().getServiceRequest();
                serviceRequests.add(serviceRequest);
            }
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
