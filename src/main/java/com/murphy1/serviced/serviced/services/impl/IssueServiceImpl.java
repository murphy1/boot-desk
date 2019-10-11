package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.model.*;
import com.murphy1.serviced.serviced.repositories.AdminRepository;
import com.murphy1.serviced.serviced.repositories.AgentRepository;
import com.murphy1.serviced.serviced.repositories.EndUserRepository;
import com.murphy1.serviced.serviced.services.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.murphy1.serviced.serviced.repositories.IssueRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class IssueServiceImpl implements IssueService {

    private IssueRepository issueRepository;
    private AdminRepository adminRepository;
    private AgentRepository agentRepository;
    private EndUserRepository endUserRepository;

    private UserService userService;

    private JavaMailSender javaMailSender;

    public IssueServiceImpl(IssueRepository issueRepository, AdminRepository adminRepository, AgentRepository agentRepository, EndUserRepository endUserRepository, UserService userService, JavaMailSender javaMailSender) {
        this.issueRepository = issueRepository;
        this.adminRepository = adminRepository;
        this.agentRepository = agentRepository;
        this.endUserRepository = endUserRepository;
        this.userService = userService;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public List<Issue> getAllIssues() {

        List<Issue> issues = new ArrayList<>();

        issueRepository.findAll().iterator().forEachRemaining(issues :: add);

        return issues;
    }

    @Override
    public Issue save(Issue issue) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = ((UserDetails)principal).getUsername();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        User currentUser = new User();
        String role = userService.getRole(username);

        String creatorString = issue.getCreator();

        // new issues will always be set to Status = NEW
        // Add the issue to the current users list of issues
        if (issue.getId() == null){
            issue.setStatus(Status.NEW);
            issue.setCreator(username);

            LocalDate date = LocalDate.now();
            String priority = issue.getPriority().toString();

            switch (priority) {
                case "VERY_HIGH":
                    issue.setDueDate(date.plusDays(1));
                    break;
                case "HIGH":
                    issue.setDueDate(date.plusDays(2));
                    break;
                case "MEDIUM":
                    issue.setDueDate(date.plusDays(5));
                    break;
                case "LOW":
                    issue.setDueDate(date.plusDays(7));
                    break;
            }

            List<Issue> usersIssues;

            if (role.equals("ADMIN")){
                Optional<Admin> adminOptional = adminRepository.findByUsername(username);
                usersIssues = adminOptional.get().getIssue();
                usersIssues.add(issue);

                currentUser = userService.convertAdminToUser(adminOptional.get());
            }
            else if (role.equals("AGENT")){
                Optional<Agent> agentOptional = agentRepository.findByUsername(username);
                usersIssues = agentOptional.get().getIssue();
                usersIssues.add(issue);

                currentUser = userService.convertAgentToUser(agentOptional.get());
            }
            else if (role.equals("END_USER")){
                Optional<EndUser> endUserOptional = endUserRepository.findByUsername(username);
                usersIssues = endUserOptional.get().getIssue();
                usersIssues.add(issue);

                currentUser = userService.convertEndUserToUser(endUserOptional.get());
            }

            var mailMessage = new SimpleMailMessage();

            mailMessage.setTo(currentUser.getEmail());
            mailMessage.setSubject("Issue created");
            mailMessage.setText("Hello "+currentUser.getFirstName()+
                    "\n\nYou have created an issue: "+issue.getName()+"\n\n"+
                    "An agent will reply soon to provide assistance.\n\n"+
                    "Please contact us again if you need further assistance."
            );

            //javaMailSender.send(mailMessage);

            // if the creator enters a username
            if (issue.getAssignedTo() != null){

                User assignedTo = userService.findUserByUsername(issue.getAssignedTo());

                var mailMessage1 = new SimpleMailMessage();

                mailMessage.setTo(assignedTo.getEmail());
                mailMessage.setSubject("You have been assigned to a new issue");
                mailMessage.setText("Hello "+assignedTo.getFirstName()+
                        "\n\nYou have been assigned to issue: "+issue.getName()+"\n\n"+
                        "Please log in to view updates."
                );

                //javaMailSender.send(mailMessage);
            }

        }
        // Send email to the creator of the ticket if someone other than the creator updates Ticket!
        else if (issue.getId() != null && !creatorString.equals(username)){

            User creator = userService.findUserByUsername(creatorString);

            var mailMessage = new SimpleMailMessage();

            mailMessage.setTo(creator.getEmail());
            mailMessage.setSubject("Your Issue "+issue.getId()+" was updated");
            mailMessage.setText("Hello "+creator.getFirstName()+
                    "\n\nYour issue: "+issue.getName()+"\n\n"+
                    "Has been updated in the system.\n\n"+
                    "Please log in to view updates."
            );

            //javaMailSender.send(mailMessage);
        }
        // send email to assigned to, if someone other than the assigned to assigns them
        else if (issue.getId() != null && issue.getAssignedTo() != null && issue.getStatus().toString().equals("ASSIGNED")){

            User assignedTo = userService.findUserByUsername(issue.getAssignedTo());

            var mailMessage = new SimpleMailMessage();

            mailMessage.setTo(assignedTo.getEmail());
            mailMessage.setSubject("You have been assigned to "+issue.getId());
            mailMessage.setText("Hello "+assignedTo.getFirstName()+
                    "\n\nYou have been assigned to issue: "+issue.getName()+"\n\n"+
                    "Please log in to view updates."
            );

            //javaMailSender.send(mailMessage);

        }
        // send email if the creator answers the assign to
        else if (issue.getId() != null && issue.getAssignedTo() != null && username.equals(issue.getCreator())){

            User assignedTo = userService.findUserByUsername(issue.getAssignedTo());

            var mailMessage = new SimpleMailMessage();

            mailMessage.setTo(assignedTo.getEmail());
            mailMessage.setSubject("The Creator has replied to issue "+issue.getId());
            mailMessage.setText("Hello "+assignedTo.getFirstName()+
                    "\n\nAs you are assigned to: "+issue.getName()+"\n\n"+
                    "Please log in to view updates from the creator."
            );

            //javaMailSender.send(mailMessage);
        }

        if (issue.getNewMessages() != null){
            String oldMessages = issue.getMessages();
            if (oldMessages == null){
                issue.setMessages(issue.getNewMessages());
            }
            else{
                String newMessage = oldMessages + "\n"+"----------"+"\n"+username+", "+dtf.format(now)+"\n"+"----------"+"\n"+ issue.getNewMessages();
                issue.setMessages(newMessage);
                oldMessages = "";
            }
        }

        return issueRepository.save(issue);
    }

    @Override
    public Issue findIssueById(Long id) {

        Optional<Issue> issueOptional = issueRepository.findById(id);

        if (issueOptional.isEmpty()){
            throw new RuntimeException("Issue does not exist!");
        }

        return issueOptional.get();
    }

    @Override
    public void deleteIssue(long id) {
        issueRepository.deleteById(id);
    }

    @Override
    public List<Issue> findIssueByUser(String username) {
        List<Issue> issues = new ArrayList<>();

        Iterable<Issue> issueIterator = issueRepository.findAll();
        for (Issue issue : issueIterator){
            if (issue.getCreator() == null){
                continue;
            }
            else if (issue.getCreator().equalsIgnoreCase(username)){
                issues.add(issue);
            }
        }

        return issues;
    }

    public List<Issue> findAssignedIssuesByUser(String username){
        List<Issue> issues = new ArrayList<>();

        Iterable<Issue> issueIterator = issueRepository.findAll();
        for (Issue issue : issueIterator){
            if (issue.getAssignedTo() == null){
                continue;
            }
            else if (issue.getAssignedTo().equalsIgnoreCase(username)){
                issues.add(issue);
            }
        }

        return issues;
    }
}
