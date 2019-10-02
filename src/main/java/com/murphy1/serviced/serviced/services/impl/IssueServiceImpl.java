package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.model.Issue;
import com.murphy1.serviced.serviced.model.Status;
import org.springframework.stereotype.Service;
import com.murphy1.serviced.serviced.repositories.IssueRepository;
import com.murphy1.serviced.serviced.services.IssueService;

import java.util.ArrayList;
import java.util.List;

@Service
public class IssueServiceImpl implements IssueService {

    private IssueRepository issueRepository;

    public IssueServiceImpl(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    @Override
    public List<Issue> getAllIssues() {

        List<Issue> issues = new ArrayList<>();

        issueRepository.findAll().iterator().forEachRemaining(issues :: add);

        return issues;
    }

    @Override
    public Issue save(Issue issue) {

        // new issues will always be set to Status = NEW
        if (issue.getId() == null){
            issue.setStatus(Status.NEW);
        }

        return issueRepository.save(issue);
    }
}
