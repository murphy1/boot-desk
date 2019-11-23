package com.murphy1.serviced.serviced.rest;

import com.murphy1.serviced.serviced.model.Issue;
import com.murphy1.serviced.serviced.model.Status;
import com.murphy1.serviced.serviced.services.IssueService;
import com.murphy1.serviced.serviced.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class RestIssuesControllerTest {

    @Mock
    IssueService issueService;

    @Mock
    UserService userService;

    MockMvc mockMvc;

    private RestIssuesController restIssuesController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        restIssuesController = new RestIssuesController(issueService, userService);
        mockMvc = MockMvcBuilders.standaloneSetup(restIssuesController).build();
    }

    @org.junit.jupiter.api.Test
    void getAllIssues() throws Exception{
        List<Issue> issues = new ArrayList<>();
        issues.add(new Issue());

        BDDMockito.given(issueService.getAllIssues())
                .willReturn(issues);

        assertEquals(issues.size(), 1);
    }

    @org.junit.jupiter.api.Test
    void getOpenIssues() throws Exception{
        Issue issue = new Issue();
        issue.setStatus(Status.NEW);

        List<Issue> issues = new ArrayList<>();
        issues.add(issue);

        BDDMockito.given(issueService.getAllIssues())
                .willReturn(issues);

        List<Issue> returnList = restIssuesController.getOpenIssues();

        verify(issueService, times(1)).getAllIssues();

        mockMvc.perform(get("/api/v1/issues/open")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @org.junit.jupiter.api.Test
    void getClosedIssues() throws Exception{
        mockMvc.perform(get("/api/v1/issues/closed")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @org.junit.jupiter.api.Test
    void getIssueById() throws Exception{
        Optional<Issue> issueOptional = Optional.of(new Issue());
        issueOptional.get().setId(5L);

        BDDMockito.given(issueService.findIssueById(anyLong()))
                .willReturn(issueOptional.get());

        assertEquals(Optional.ofNullable(issueOptional.get().getId()), Optional.of(5L));

        mockMvc.perform(get("/api/v1/issues/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}