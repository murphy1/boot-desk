package com.murphy1.serviced.serviced.controllers;

import com.murphy1.serviced.serviced.model.ServiceRequest;
import com.murphy1.serviced.serviced.model.Status;
import com.murphy1.serviced.serviced.services.ServiceRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/service_requests/new")
    public String newServiceRequest(Model model){
        model.addAttribute("serviceRequest", new ServiceRequest());

        return "/forms/new_servicerequest.html";
    }

    @GetMapping("/service_requests/update/{requestId}")
    public String updateServiceRequest(@PathVariable String requestId, Model model){
        model.addAttribute("serviceRequest", serviceRequestService.findById(Long.valueOf(requestId)));

        return "/forms/new_servicerequest.html";
    }

    @GetMapping("/service_requests/delete/{requestId}")
    public String deleteServiceRequest(@PathVariable String requestId){
        serviceRequestService.deleteServiceRequest(Long.valueOf(requestId));

        return "redirect:/service_requests";
    }

    @PostMapping("/serviceRequest/save")
    public String saveServiceRequest(@ModelAttribute ServiceRequest serviceRequest){
        serviceRequestService.save(serviceRequest);

        return "redirect:/service_requests";
    }

}
