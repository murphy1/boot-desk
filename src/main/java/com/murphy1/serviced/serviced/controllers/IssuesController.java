package com.murphy1.serviced.serviced.controllers;

import com.murphy1.serviced.serviced.exceptions.BadRequestException;
import com.murphy1.serviced.serviced.model.Issue;
import com.murphy1.serviced.serviced.model.ServiceRequest;
import com.murphy1.serviced.serviced.services.ConversionService;
import com.murphy1.serviced.serviced.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.murphy1.serviced.serviced.services.IssueService;

import javax.validation.Valid;

@Slf4j
@Controller
public class IssuesController {

    private IssueService issueService;
    private UserService userService;
    private ConversionService conversionService;

    public IssuesController(IssueService issueService, UserService userService, ConversionService conversionService) {
        this.issueService = issueService;
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @RequestMapping("/issues")
    public String issueList(Model model){

        model.addAttribute("issues", issueService.getAllIssues());

        return "issues.html";
    }

    @GetMapping("/issues/view/{issueId}")
    public String viewIssue(Model model, @PathVariable String issueId){
        model.addAttribute("issues", issueService.findIssueById(Long.valueOf(issueId)));

        return "issues.html";
    }

    @GetMapping("/issues/new")
    public String newIssue(Model model){
        model.addAttribute("issue", new Issue());

        return "forms/new_issue.html";
    }

    @GetMapping("/issues/update/{issueId}")
    public String updateIssue(@PathVariable String issueId, Model model){
        Issue issue = issueService.findIssueById(Long.valueOf(issueId));
        if (issue.getStatus().toString().equals("SOLVED")){
            throw new BadRequestException("Tickets in status Solved cannot be updated");
        }
        model.addAttribute("issue", issue);

        return "forms/new_issue.html";
    }

    @GetMapping("/issues/delete/{issueId}")
    public String deleteIssue(@PathVariable String issueId){
        issueService.deleteIssue(Long.valueOf(issueId));

        return "redirect:/issues";
    }

    @PostMapping("/issue/save")
    public String saveIssue(@Valid @ModelAttribute("issue") Issue issue, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError ->
                    log.debug(objectError.toString())
                    );
            return "forms/new_issue.html";
        }

        issueService.save(issue);

        return "redirect:/issues";
    }

    @GetMapping("/issues/assign/self/{issueId}")
    public String assignMe(@PathVariable String issueId){

        Issue issue = issueService.findIssueById(Long.valueOf(issueId));

        issue.setAssignedTo(userService.getCurrentUserName());

        issueService.save(issue);

        return "redirect:/issues/view/"+issueId;
    }

    @GetMapping("/issues/convert/{issueId}")
    public String convert(@PathVariable String issueId){
        ServiceRequest serviceRequest = conversionService.convertIssueToServiceRequest(issueService.findIssueById(Long.valueOf(issueId)));

        return "redirect:/service_requests/update/"+serviceRequest.getId();
    }

}
