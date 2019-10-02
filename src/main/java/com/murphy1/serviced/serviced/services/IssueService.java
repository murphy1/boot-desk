package com.murphy1.serviced.serviced.services;

import com.murphy1.serviced.serviced.model.Issue;

import java.util.List;

public interface IssueService {
    List<Issue> getAllIssues();
    Issue save(Issue issue);
}
