package com.murphy1.serviced.serviced.services;

import com.murphy1.serviced.serviced.model.ResetToken;
import com.murphy1.serviced.serviced.model.User;

public interface MailService {
    void newUser(User endUser);
    void newTicket(User currentUser, String ticketName);
    void newTicketWithUsername(User assignedTo, String ticketName);
    void messageToCreator(User creator, String ticketName, Long ticketId);
    void messageToAssignedTo(User assignedTo, String ticketName, Long ticketId);
    void messageFromCreatorToAssignedTo(User assignedTo, String ticketName, Long ticketId);
    void forgotPassword(User user, ResetToken token);
}
