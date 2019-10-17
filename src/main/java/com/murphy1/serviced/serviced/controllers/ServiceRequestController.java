package com.murphy1.serviced.serviced.controllers;

import com.murphy1.serviced.serviced.exceptions.BadRequestException;
import com.murphy1.serviced.serviced.model.Issue;
import com.murphy1.serviced.serviced.model.ServiceRequest;
import com.murphy1.serviced.serviced.services.ConversionService;
import com.murphy1.serviced.serviced.services.ServiceRequestService;
import com.murphy1.serviced.serviced.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Slf4j
@Controller
public class ServiceRequestController {

    private ServiceRequestService serviceRequestService;
    private UserService userService;
    private ConversionService conversionService;

    public ServiceRequestController(ServiceRequestService serviceRequestService, UserService userService, ConversionService conversionService) {
        this.serviceRequestService = serviceRequestService;
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @GetMapping("/service_requests")
    public String getAllServiceRequests(Model model){
        model.addAttribute("serviceRequests", serviceRequestService.getAllServiceRequests());

        return "service_requests.html";
    }

    @GetMapping("/service_requests/view/{requestId}")
    public String viewServiceRequest(Model model, @PathVariable String requestId){
        model.addAttribute("serviceRequests", serviceRequestService.findById(Long.valueOf(requestId)));

        return "service_requests.html";
    }

    @GetMapping("/service_requests/new")
    public String newServiceRequest(Model model){
        model.addAttribute("serviceRequest", new ServiceRequest());

        return "/forms/new_servicerequest.html";
    }

    @GetMapping("/service_requests/update/{requestId}")
    public String updateServiceRequest(@PathVariable String requestId, Model model){
        ServiceRequest serviceRequest = serviceRequestService.findById(Long.valueOf(requestId));
        if (serviceRequest.getStatus().toString().equals("SOLVED")){
            throw new BadRequestException("Tickets in status Solved cannot be updated");
        }
        model.addAttribute("serviceRequest", serviceRequest);

        return "/forms/new_servicerequest.html";
    }

    @GetMapping("/service_requests/delete/{requestId}")
    public String deleteServiceRequest(@PathVariable String requestId){
        serviceRequestService.deleteServiceRequest(Long.valueOf(requestId));

        return "redirect:/service_requests";
    }

    @PostMapping("/serviceRequest/save")
    public String saveServiceRequest(@Valid @ModelAttribute("serviceRequest") ServiceRequest serviceRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError ->
                    log.debug(objectError.toString())
                    );
            return "/forms/new_servicerequest.html";
        }

        serviceRequestService.save(serviceRequest);

        return "redirect:/service_requests/view/"+serviceRequest.getId();
    }

    @GetMapping("/service_requests/assign/self/{requestId}")
    public String assignMe(@PathVariable String requestId){

        ServiceRequest serviceRequest = serviceRequestService.findById(Long.valueOf(requestId));
        serviceRequest.setAssignedTo(userService.getCurrentUserName());
        serviceRequestService.save(serviceRequest);

        return "redirect:/service_requests/view/"+requestId;
    }

    @GetMapping("/service_requests/convert/{requestId}")
    public String convert(@PathVariable String requestId){
        Issue issue = conversionService.convertServiceRequestToIssue(serviceRequestService.findById(Long.valueOf(requestId)));

        return "redirect:/issues/update/"+issue.getId().toString();
    }

}
