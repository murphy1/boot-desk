package com.murphy1.serviced.serviced.controllers;

import com.murphy1.serviced.serviced.services.ServiceRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ServiceRequestController {

    private ServiceRequestService serviceRequestService;

    public ServiceRequestController(ServiceRequestService serviceRequestService) {
        this.serviceRequestService = serviceRequestService;
    }

    @GetMapping("/service_requests")
    public String getAllServiceRequests(Model model){
        model.addAttribute("serviceRequests", serviceRequestService.getAllServiceRequests());

        return "service_requests.html";
    }

}
