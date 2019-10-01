package com.murphy1.serviced.serviced.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

}
