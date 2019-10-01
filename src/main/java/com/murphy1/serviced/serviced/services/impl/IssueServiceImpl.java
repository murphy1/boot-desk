package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.model.Issue;
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
}
