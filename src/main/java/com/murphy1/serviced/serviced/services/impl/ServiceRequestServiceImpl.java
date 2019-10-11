package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.model.*;
import com.murphy1.serviced.serviced.repositories.AdminRepository;
import com.murphy1.serviced.serviced.repositories.AgentRepository;
import com.murphy1.serviced.serviced.repositories.EndUserRepository;
import com.murphy1.serviced.serviced.services.UserService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.murphy1.serviced.serviced.repositories.ServiceRequestRepository;
import com.murphy1.serviced.serviced.services.ServiceRequestService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private JavaMailSender javaMailSender;

    public ServiceRequestServiceImpl(ServiceRequestRepository serviceRequestRepository, AdminRepository adminRepository, AgentRepository agentRepository, EndUserRepository endUserRepository, UserService userService, JavaMailSender javaMailSender) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.adminRepository = adminRepository;
        this.agentRepository = agentRepository;
        this.endUserRepository = endUserRepository;
        this.userService = userService;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public List<ServiceRequest> getAllServiceRequests() {

        List<ServiceRequest> serviceRequests = new ArrayList<>();

        serviceRequestRepository.findAll().iterator().forEachRemaining(serviceRequests :: add);

        return serviceRequests;
    }

    @Override
    public ServiceRequest save(ServiceRequest serviceRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String username = ((UserDetails) principal).getUsername();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        // for email notifications
        User currentUser = new User();
        String creatorString = serviceRequest.getCreator();

        if (serviceRequest.getId() == null){
            serviceRequest.setStatus(Status.NEW);
            serviceRequest.setCreator(userService.getCurrentUserName());

            String role = userService.getRole(username);

            LocalDate date = LocalDate.now();
            String priority = serviceRequest.getPriority().toString();

            switch (priority) {
                case "VERY_HIGH":
                    serviceRequest.setDueDate(date.plusDays(1));
                    break;
                case "HIGH":
                    serviceRequest.setDueDate(date.plusDays(2));
                    break;
                case "MEDIUM":
                    serviceRequest.setDueDate(date.plusDays(5));
                    break;
                case "LOW":
                    serviceRequest.setDueDate(date.plusDays(7));
                    break;
            }

            List<ServiceRequest> serviceRequests;

            if (role.equals("ADMIN")){
                Optional<Admin> adminOptional = adminRepository.findByUsername(username);
                serviceRequests = adminOptional.get().getServiceRequest();
                serviceRequests.add(serviceRequest);

                currentUser = userService.convertAdminToUser(adminOptional.get());
            }
            else if (role.equals("AGENT")){
                Optional<Agent> agentOptional = agentRepository.findByUsername(username);
                serviceRequests = agentOptional.get().getServiceRequest();
                serviceRequests.add(serviceRequest);

                currentUser = userService.convertAgentToUser(agentOptional.get());
            }
            else if (role.equals("END_USER")){
                Optional<EndUser> endUserOptional = endUserRepository.findByUsername(username);
                serviceRequests = endUserOptional.get().getServiceRequest();
                serviceRequests.add(serviceRequest);

                currentUser = userService.convertEndUserToUser(endUserOptional.get());
            }

            var mailMessage = new SimpleMailMessage();

            mailMessage.setTo(currentUser.getEmail());
            mailMessage.setSubject("Service Request created");
            mailMessage.setText("Hello "+currentUser.getFirstName()+
                    "\n\nYou have created a Service Request: "+serviceRequest.getName()+"\n\n"+
                    "An agent will reply soon to provide assistance.\n\n"+
                    "Please contact us again if you need further assistance."
            );

            //javaMailSender.send(mailMessage);

            // if the creator enters a username
            if (!serviceRequest.getAssignedTo().equals("")){

                User assignedTo = userService.findUserByUsername(serviceRequest.getAssignedTo());

                var mailMessage1 = new SimpleMailMessage();

                mailMessage.setTo(assignedTo.getEmail());
                mailMessage.setSubject("You have been assigned to a new service request");
                mailMessage.setText("Hello "+assignedTo.getFirstName()+
                        "\n\nYou have been assigned to service request: "+serviceRequest.getName()+"\n\n"+
                        "Please log in to view updates."
                );

                //javaMailSender.send(mailMessage);
            }

        }
        // Send email to the creator of the ticket if someone other than the creator updates Ticket!
        else if (serviceRequest.getId() != null && !creatorString.equals(username)){

            User creator = userService.findUserByUsername(creatorString);

            var mailMessage = new SimpleMailMessage();

            mailMessage.setTo(creator.getEmail());
            mailMessage.setSubject("Your Service Request "+serviceRequest.getId()+" was updated");
            mailMessage.setText("Hello "+creator.getFirstName()+
                    "\n\nYour service request: "+serviceRequest.getName()+"\n\n"+
                    "Has been updated in the system.\n\n"+
                    "Please log in to view updates."
            );

            //javaMailSender.send(mailMessage);
        }
        // send email to assigned to, if someone other than the assigned to assigns them
        else if (serviceRequest.getId() != null && !serviceRequest.getAssignedTo().equals("") && serviceRequest.getStatus().toString().equals("ASSIGNED")){

            User assignedTo = userService.findUserByUsername(serviceRequest.getAssignedTo());

            var mailMessage = new SimpleMailMessage();

            mailMessage.setTo(assignedTo.getEmail());
            mailMessage.setSubject("You have been assigned to "+serviceRequest.getId());
            mailMessage.setText("Hello "+assignedTo.getFirstName()+
                    "\n\nYou have been assigned to service request: "+serviceRequest.getName()+"\n\n"+
                    "Please log in to view updates."
            );

            //javaMailSender.send(mailMessage);

        }
        // send email if the creator answers the assign to
        else if (serviceRequest.getId() != null && !serviceRequest.getAssignedTo().equals("") && username.equals(serviceRequest.getCreator())){

            User assignedTo = userService.findUserByUsername(serviceRequest.getAssignedTo());

            var mailMessage = new SimpleMailMessage();

            mailMessage.setTo(assignedTo.getEmail());
            mailMessage.setSubject("The Creator has replied to service request "+serviceRequest.getId());
            mailMessage.setText("Hello "+assignedTo.getFirstName()+
                    "\n\nAs you are assigned to: "+serviceRequest.getName()+"\n\n"+
                    "Please log in to view updates from the creator."
            );

            //javaMailSender.send(mailMessage);
        }
        // creator updates ticket with no Assigned To
        else{

        }

        if (serviceRequest.getNewMessages() != null){
            String oldMessages = serviceRequest.getMessages();
            if (oldMessages == null){
                serviceRequest.setMessages(serviceRequest.getNewMessages());
            }
            else{
                String newMessage = oldMessages + "\n"+"----------"+"\n"+username+", "+dtf.format(now)+"\n"+"----------"+"\n"+ serviceRequest.getNewMessages();
                serviceRequest.setMessages(newMessage);
                oldMessages = "";
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

    @Override
    public List<ServiceRequest> findRequestByUser(String username) {
        List<ServiceRequest> requests = new ArrayList<>();

        Iterable<ServiceRequest> iterable = serviceRequestRepository.findAll();
        for (ServiceRequest request : iterable){
            if (request.getCreator() == null){
                continue;
            }
            else if (request.getCreator().equalsIgnoreCase(username)){
                requests.add(request);
            }
        }

        return requests;
    }

    @Override
    public List<ServiceRequest> findRequestByAssignedUser(String username) {
        List<ServiceRequest> requests = new ArrayList<>();

        Iterable<ServiceRequest> iterable = serviceRequestRepository.findAll();
        for (ServiceRequest request : iterable){
            if (request.getAssignedTo() == null){
                continue;
            }
            else if (request.getAssignedTo().equalsIgnoreCase(username)){
                requests.add(request);
            }
        }
        return requests;
    }
}
