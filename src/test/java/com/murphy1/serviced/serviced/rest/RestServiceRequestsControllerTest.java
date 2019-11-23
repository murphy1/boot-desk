package com.murphy1.serviced.serviced.rest;

import com.murphy1.serviced.serviced.model.ServiceRequest;
import com.murphy1.serviced.serviced.model.Status;
import com.murphy1.serviced.serviced.services.ServiceRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestServiceRequestsControllerTest {

    @Mock
    private ServiceRequestService serviceRequestService;

    private RestServiceRequestsController restServiceRequestsController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        restServiceRequestsController = new RestServiceRequestsController(serviceRequestService);
        mockMvc = MockMvcBuilders.standaloneSetup(restServiceRequestsController).build();
    }

    @Test
    void getAllServiceRequests() throws Exception {
        List<ServiceRequest> serviceRequests = new ArrayList<>();
        serviceRequests.add(new ServiceRequest());
        serviceRequests.add(new ServiceRequest());

        when(serviceRequestService.getAllServiceRequests()).thenReturn(serviceRequests);

        assertEquals(2, serviceRequests.size());

        mockMvc.perform(get("/api/v1/service_requests")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllOpenServiceRequests() throws Exception{
        mockMvc.perform(get("/api/v1/service_requests/open")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllClosedServiceRequests() throws Exception{
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setStatus(Status.SOLVED);
        ServiceRequest serviceRequest1 = new ServiceRequest();
        serviceRequest1.setStatus(Status.NEW);

        List<ServiceRequest> serviceRequests = new ArrayList<>();
        serviceRequests.add(serviceRequest);
        serviceRequests.add(serviceRequest1);

        when(serviceRequestService.getAllServiceRequests()).thenReturn(serviceRequests);

        serviceRequests.removeIf(serviceRequest2 -> !serviceRequest2.getStatus().equals(Status.SOLVED));

        assertEquals(1, serviceRequests.size());

        mockMvc.perform(get("/api/v1/service_requests/closed")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getServiceRequestById() throws Exception{
        mockMvc.perform(get("/api/v1/service_requests/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}