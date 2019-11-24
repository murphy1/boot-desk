package com.murphy1.serviced.serviced.rest;

import com.murphy1.serviced.serviced.model.Team;
import com.murphy1.serviced.serviced.model.User;
import com.murphy1.serviced.serviced.services.*;
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

class RestAnalyticsControllerTest {

    @Mock
    private TeamService teamService;
    @Mock
    private AnalyticsService analyticsService;
    @Mock
    private IssueService issueService;
    @Mock
    private ServiceRequestService serviceRequestService;
    @Mock
    private UserService userService;

    private RestAnalyticsController restAnalyticsController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        restAnalyticsController = new RestAnalyticsController(teamService, analyticsService, issueService, serviceRequestService, userService);
        mockMvc = MockMvcBuilders.standaloneSetup(restAnalyticsController).build();
    }

    @Test
    void getAllTeams() throws Exception{
        mockMvc.perform(get("/api/v1/analytics/teams")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getTeamById() throws Exception{
        Team team = new Team();
        team.setId(432L);

        List<Team> teams = new ArrayList<>();
        teams.add(team);

        when(teamService.getAllTeams())
                .thenReturn(teams);

        Team teamToCheck = teams.stream()
                .filter(team1 -> team1.getId().equals(432L))
                .findFirst()
                .get();

        assertEquals(432L, teamToCheck.getId().longValue());

        mockMvc.perform(get("/api/v1/analytics/teams/432")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllOverdueTickets() throws Exception{
        mockMvc.perform(get("/api/v1/analytics/overdue_tickets")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllUsers() throws Exception{
        User user = new User();
        user.setFirstName("Tester");

        List<User> users = new ArrayList<>();
        users.add(user);

        when(userService.getAllUsers()).thenReturn(users);

        assertEquals("Tester", users.get(0).getFirstName());

        mockMvc.perform(get("/api/v1/analytics/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}