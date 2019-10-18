package com.murphy1.serviced.serviced.controllers;

import com.murphy1.serviced.serviced.model.Ticket;
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

    // Variable to keep an eye on adding more search criteria
    private int count;

    public SearchController(SearchService searchService) {
        this.count = 0;
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public String search(Model model){
        model.addAttribute("searchObject", new Search());

        return "forms/searchform.html";
    }

    @PostMapping("/search/submit_search")
    public String searchResults(Model model, @ModelAttribute Search search){
        // reset the count if a search is submitted
        count = 0;

        List<Ticket> issues;
        List<Ticket> serviceRequests;

        String object = search.getSearchObject().toString();
        String query = search.getSearchQuery();

        String object1;
        String query1;

        try{
            object1 = search.getSearchObject1().toString();
            query1 = search.getSearchQuery1();
        }catch (NullPointerException e){
            object1 = null;
            query1 = null;
        }

        if (search.getSearchType().toString().equals("ISSUE")){
            if (object1 == null){
                issues = searchService.issueResult(object, query);
            }
            else{
                issues = searchService.issueResult(object, query, object1, query1);
            }

            if (!issues.isEmpty()){
                model.addAttribute("issues", issues);
                model.addAttribute("serviceRequests", "EMPTY");
            }
            else {
                model.addAttribute("issues", "EMPTY");
                model.addAttribute("serviceRequests", "EMPTY");
            }
        }
        else if (search.getSearchType().toString().equals("SERVICE_REQUEST")){
            if (object1 == null){
                serviceRequests = searchService.serviceRequestResult(object, query);
            }
            else{
                serviceRequests = searchService.serviceRequestResult(object, query, object1, query1);
            }
            if (!serviceRequests.isEmpty()){
                model.addAttribute("issues", "EMPTY");
                model.addAttribute("serviceRequests", serviceRequests);
            }
            else {
                model.addAttribute("issues", "EMPTY");
                model.addAttribute("serviceRequests", "EMPTY");
            }
        }
        else{
            throw new RuntimeException("Search object does not exist!");
        }

        return "/searchresults";
    }

    @GetMapping("/search/more_criteria")
    public String addMoreCriteria(Model model){

        count++;
        model.addAttribute("searchObject", new Search());
        model.addAttribute("count", count);

        return "/forms/searchform.html";
    }

}
