package com.murphy1.serviced.serviced.services;

import com.murphy1.serviced.serviced.model.Team;

import java.util.List;

public interface AnalyticsService {
    Long getAllOpenTickets();
    Long overDueTickets();

    Long openTicketsByUsername(String username);
    Long ticketsClosedByUsername(String username);

    Long allUsers();
    Long allAdmins();
    Long allAgents();
    Long allEndUsers();

    Long ticketsByLabel(String label);

    List<Team> getAllTeams();
}
