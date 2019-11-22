package com.murphy1.serviced.serviced.rest;

import com.murphy1.serviced.serviced.services.IssueService;
import com.murphy1.serviced.serviced.services.ServiceRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
class RestTicketControllerTest {

    @Mock
    private IssueService issueService;
    @Mock
    private ServiceRequestService serviceRequestService;

    MockMvc mockMvc;

    private RestTicketController restTicketController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        restTicketController = new RestTicketController(issueService, serviceRequestService);
        mockMvc = MockMvcBuilders.standaloneSetup(restTicketController).build();
    }

    @Test
    void getAllTickets() throws Exception{
        mockMvc.perform(get("/api/v1/tickets")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllOpenTickets() throws Exception{
        mockMvc.perform(get("/api/v1/tickets/open")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllClosedTickets() throws Exception{
        mockMvc.perform(get("/api/v1/tickets/closed")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}