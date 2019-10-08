package com.murphy1.serviced.serviced.services;

import com.murphy1.serviced.serviced.model.Issue;
import com.murphy1.serviced.serviced.model.User;

import java.util.List;

public interface IssueService {
    List<Issue> getAllIssues();
    Issue save(Issue issue);
    Issue findIssueById(Long id);
    void deleteIssue(long id);
    List<Issue> findIssueByUser(String username);
    List<Issue> findAssignedIssuesByUser(String username);
}
