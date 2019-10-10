package com.murphy1.serviced.serviced.controllers;

import com.murphy1.serviced.serviced.model.Issue;
import com.murphy1.serviced.serviced.model.ServiceRequest;
import com.murphy1.serviced.serviced.searching.Search;
import com.murphy1.serviced.serviced.services.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class SearchController {

    private SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public String search(Model model){
        model.addAttribute("searchObject", new Search());

        return "forms/searchform.html";
    }

    @PostMapping("/search/submit_search")
    public String searchResults(Model model, @ModelAttribute Search search){
        List<Issue> issues;
        List<ServiceRequest> serviceRequests;

        String object = search.getSearchObject().toString();
        String query = search.getSearchQuery();

        if (search.getSearchType().toString().equals("ISSUE")){
            model.addAttribute("results", issues = searchService.issueResult(object, query));
        }
        else if (search.getSearchType().toString().equals("SERVICE_REQUEST")){
            model.addAttribute("results", serviceRequests = searchService.serviceRequestResult(object, query));
        }
        else{
            throw new RuntimeException("Search object does not exist!");
        }

        return "/searchresults";
    }

}
