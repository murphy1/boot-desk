package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.exceptions.BadRequestException;
import com.murphy1.serviced.serviced.exceptions.NotFoundException;
import com.murphy1.serviced.serviced.model.*;
import com.murphy1.serviced.serviced.repositories.*;
import com.murphy1.serviced.serviced.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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
    private TeamsRepository teamsRepository;

    private UserService userService;
    private MailService mailService;

    public IssueServiceImpl(IssueRepository issueRepository, AdminRepository adminRepository, AgentRepository agentRepository, EndUserRepository endUserRepository, TeamsRepository teamsRepository, UserService userService, MailService mailService) {
        this.issueRepository = issueRepository;
        this.adminRepository = adminRepository;
        this.agentRepository = agentRepository;
        this.endUserRepository = endUserRepository;
        this.teamsRepository = teamsRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    @Override
    public List<Issue> getAllIssues() {

        List<Issue> issues = new ArrayList<>();

        issueRepository.findAll().iterator().forEachRemaining(issues :: add);

        return issues;
    }

    @Override
    public Issue save(Issue issue) {

        // If Assigned To is an End User throw an error. Only Admins and Agents can be assigned
        if (!issue.getAssignedTo().equals("")){
            User user = userService.findUserByUsername(issue.getAssignedTo());
            if (user.getRoles().equals("END_USER")){
                throw new BadRequestException("End Users cannot be assigned to Tickets!");
            }
        }

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

            mailService.newTicket(currentUser, issue.getName());

            // if the creator enters a username
            if (!issue.getAssignedTo().equals("")){

                User assignedTo = userService.findUserByUsername(issue.getAssignedTo());
                mailService.newTicketWithUsername(assignedTo, issue.getName());

            }

        }
        // Send email to the creator of the ticket if someone other than the creator updates Ticket!
        else if (issue.getId() != null && !creatorString.equals(username)){

            User creator = userService.findUserByUsername(creatorString);
            mailService.messageToCreator(creator, issue.getName(), issue.getId());

        }
        // send email to assigned to, if someone other than the assigned to assigns them
        else if (issue.getId() != null && !issue.getAssignedTo().equals("") && issue.getStatus().toString().equals("ASSIGNED")){

            User assignedTo = userService.findUserByUsername(issue.getAssignedTo());
            mailService.messageToAssignedTo(assignedTo, issue.getName(), issue.getId());

        }
        // send email if the creator answers the assign to
        else if (issue.getId() != null && !issue.getAssignedTo().equals("") && username.equals(issue.getCreator())){

            User assignedTo = userService.findUserByUsername(issue.getAssignedTo());
            mailService.messageFromCreatorToAssignedTo(assignedTo, issue.getName(), issue.getId());

        }
        // creator updates ticket with no Assigned To
        else{

        }

        // Set Messages

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

        // If Status changes to SOLVED, Update the target in the users Team
        if (issue.getStatus().toString().equals("SOLVED")){

            String assignedToUsername = issue.getAssignedTo();
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
                Optional<Agent> agentAssignedTo = agentRepository.findByUsername(issue.getAssignedTo());
                if (agentAssignedTo.get().getTeam() != null){
                    Team team = agentAssignedTo.get().getTeam();
                    Long progressToTarget = team.getProgressToTarget();
                    team.setProgressToTarget(progressToTarget + 1);
                    teamsRepository.save(team);
                }
            }
        }

        return issueRepository.save(issue);
    }

    @Override
    public Issue findIssueById(Long id) {

        Optional<Issue> issueOptional = issueRepository.findById(id);

        if (issueOptional.isEmpty()){
            throw new NotFoundException("Issue does not exist!");
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
            else if (issue.getCreator().equalsIgnoreCase(username) && !issue.getStatus().toString().equals("SOLVED")){
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
            else if (issue.getAssignedTo().equalsIgnoreCase(username) && !issue.getStatus().toString().equals("SOLVED")){
                issues.add(issue);
            }
        }

        return issues;
    }
}
