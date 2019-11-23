package com.murphy1.serviced.serviced.rest;

import com.murphy1.serviced.serviced.model.Issue;
import com.murphy1.serviced.serviced.model.Status;
import com.murphy1.serviced.serviced.services.IssueService;
import com.murphy1.serviced.serviced.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RestIssuesController {

    private IssueService issueService;
    private UserService userService;

    public RestIssuesController(IssueService issueService, UserService userService) {
        this.issueService = issueService;
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/issues")
    public List<Issue> getAllIssues(){
        return issueService.getAllIssues();
    }

    @GetMapping("/api/v1/issues/open")
    public List<Issue> getOpenIssues(){
        return issueService.getAllIssues().stream()
                .filter(issue -> !issue.getStatus().equals(Status.SOLVED))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v1/issues/closed")
    public List<Issue> getClosedIssues(){
        return issueService.getAllIssues().stream()
                .filter(issue -> issue.getStatus().equals(Status.SOLVED))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v1/issues/{id}")
    public Issue getIssueById(@PathVariable String id){
        return issueService.findIssueById(Long.valueOf(id));
    }
}
