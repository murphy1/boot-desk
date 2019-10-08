package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.model.*;
import com.murphy1.serviced.serviced.repositories.AdminRepository;
import com.murphy1.serviced.serviced.repositories.AgentRepository;
import com.murphy1.serviced.serviced.repositories.EndUserRepository;
import com.murphy1.serviced.serviced.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.murphy1.serviced.serviced.repositories.IssueRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class IssueServiceImpl implements IssueService {

    private IssueRepository issueRepository;
    private AdminRepository adminRepository;
    private AgentRepository agentRepository;
    private EndUserRepository endUserRepository;

    private UserService userService;

    public IssueServiceImpl(IssueRepository issueRepository, AdminRepository adminRepository, AgentRepository agentRepository, EndUserRepository endUserRepository, UserService userService) {
        this.issueRepository = issueRepository;
        this.adminRepository = adminRepository;
        this.agentRepository = agentRepository;
        this.endUserRepository = endUserRepository;
        this.userService = userService;
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

        // new issues will always be set to Status = NEW
        // Add the issue to the current users list of issues
        if (issue.getId() == null){
            issue.setStatus(Status.NEW);
            issue.setCreator(username);

            String role = userService.getRole(username);

            List<Issue> usersIssues;

            if (role.equals("ADMIN")){
                Optional<Admin> adminOptional = adminRepository.findByUsername(username);
                usersIssues = adminOptional.get().getIssue();
                usersIssues.add(issue);
            }
            else if (role.equals("AGENT")){
                Optional<Agent> agentOptional = agentRepository.findByUsername(username);
                usersIssues = agentOptional.get().getIssue();
                usersIssues.add(issue);
            }
            else if (role.equals("END_USER")){
                Optional<EndUser> endUserOptional = endUserRepository.findByUsername(username);
                usersIssues = endUserOptional.get().getIssue();
                usersIssues.add(issue);
            }
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
