package com.murphy1.serviced.serviced.controllers;

import com.murphy1.serviced.serviced.model.Issue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.murphy1.serviced.serviced.services.IssueService;

@Controller
public class IssuesController {

    private IssueService issueService;

    public IssuesController(IssueService issueService) {
        this.issueService = issueService;
    }

    @RequestMapping("/issues")
    public String issueList(Model model){

        model.addAttribute("issues", issueService.getAllIssues());

        return "issues.html";
    }

    @GetMapping("/issues/new")
    public String newIssue(Model model){
        model.addAttribute("issue", new Issue());

        return "forms/new_issue.html";
    }

    @PostMapping("/issue/save")
    public String saveIssue(@ModelAttribute Issue issue){
        issueService.save(issue);

        return "redirect:/issues";
    }

}
