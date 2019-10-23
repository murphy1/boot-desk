package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.exceptions.NotFoundException;
import com.murphy1.serviced.serviced.model.*;
import com.murphy1.serviced.serviced.repositories.*;
import com.murphy1.serviced.serviced.services.MailService;
import com.murphy1.serviced.serviced.services.UserService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
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
    private TeamsRepository teamsRepository;

    private UserService userService;
    private MailService mailService;

    private JavaMailSender javaMailSender;

    public ServiceRequestServiceImpl(ServiceRequestRepository serviceRequestRepository, AdminRepository adminRepository, AgentRepository agentRepository, EndUserRepository endUserRepository, TeamsRepository teamsRepository, UserService userService, MailService mailService, JavaMailSender javaMailSender) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.adminRepository = adminRepository;
        this.agentRepository = agentRepository;
        this.endUserRepository = endUserRepository;
        this.teamsRepository = teamsRepository;
        this.userService = userService;
        this.mailService = mailService;
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

            mailService.newTicket(currentUser, serviceRequest.getName());

            // if the creator enters a username
            if (!serviceRequest.getAssignedTo().equals("")){

                User assignedTo = userService.findUserByUsername(serviceRequest.getAssignedTo());
                mailService.newTicketWithUsername(assignedTo, serviceRequest.getName());

            }

        }
        // Send email to the creator of the ticket if someone other than the creator updates Ticket!
        else if (serviceRequest.getId() != null && !creatorString.equals(username)){

            User creator = userService.findUserByUsername(creatorString);
            mailService.messageToCreator(creator, serviceRequest.getName(), serviceRequest.getId());

        }
        // send email to assigned to, if someone other than the assigned to assigns them
        else if (serviceRequest.getId() != null && !serviceRequest.getAssignedTo().equals("") && serviceRequest.getStatus().toString().equals("ASSIGNED")){

            User assignedTo = userService.findUserByUsername(serviceRequest.getAssignedTo());
            mailService.messageToAssignedTo(assignedTo, serviceRequest.getName(), serviceRequest.getId());

        }
        // send email if the creator answers the assign to
        else if (serviceRequest.getId() != null && !serviceRequest.getAssignedTo().equals("") && username.equals(serviceRequest.getCreator())){

            User assignedTo = userService.findUserByUsername(serviceRequest.getAssignedTo());
            mailService.messageFromCreatorToAssignedTo(assignedTo, serviceRequest.getName(), serviceRequest.getId());

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

        // If Status changes to SOLVED, Update the target in the users Team
        if (serviceRequest.getStatus().toString().equals("SOLVED")){

            String assignedToUsername = serviceRequest.getAssignedTo();
            User user = userService.findUserByUsername(assignedToUsername);

            if (user.getRoles().equals("ADMIN")){
                Optional<Admin> adminOptional = adminRepository.findByUsername(assignedToUsername);
                if (adminOptional.get().getTeam() != null){
                    Team team = adminOptional.get().getTeam();
                    Long progressToTarget = team.getProgressToTarget();
                    team.setProgressToTarget(progressToTarget + 1);
                    teamsRepository.save(team);
                }
            }
            else if (user.getRoles().equals("AGENT")){
                Optional<Agent> agentAssignedTo = agentRepository.findByUsername(serviceRequest.getAssignedTo());
                if (agentAssignedTo.get().getTeam() != null){
                    Team team = agentAssignedTo.get().getTeam();
                    Long progressToTarget = team.getProgressToTarget();
                    team.setProgressToTarget(progressToTarget + 1);
                    teamsRepository.save(team);
                }
            }
        }

        return serviceRequestRepository.save(serviceRequest);
    }

    @Override
    public ServiceRequest findById(Long id) {

        Optional<ServiceRequest> serviceRequest = serviceRequestRepository.findById(id);

        if (serviceRequest.isEmpty()){
            throw new NotFoundException("Service Request does not exist!");
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
            else if (request.getCreator().equalsIgnoreCase(username) && !request.getStatus().toString().equals("SOLVED")){
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
            else if (request.getAssignedTo().equalsIgnoreCase(username) && !request.getStatus().toString().equals("SOLVED")){
                requests.add(request);
            }
        }
        return requests;
    }
}
