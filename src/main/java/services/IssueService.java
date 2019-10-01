package services;

import model.Issue;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IssueService {
    List<Issue> getAllIssues();
}
