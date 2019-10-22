package com.murphy1.serviced.serviced.services;

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
}
