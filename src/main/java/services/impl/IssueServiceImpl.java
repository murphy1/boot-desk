package services.impl;

import model.Issue;
import model.Ticket;
import org.springframework.stereotype.Service;
import repositories.IssueRepository;
import repositories.TicketRepository;
import services.IssueService;

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
