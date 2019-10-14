package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.model.User;
import com.murphy1.serviced.serviced.services.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

    private JavaMailSender javaMailSender;

    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void newUser(User endUser) {
        var mailMessage = new SimpleMailMessage();

        mailMessage.setTo(endUser.getEmail());
        mailMessage.setSubject("Thank you for registering!");
        mailMessage.setText("Hello "+endUser.getFirstName()+
                " and welcome to Boot Desk!\n\n" +
                "You can now open Tickets.\n\n"+
                "Please contact your admin if you require anything other than End User access."
        );
        log.debug("Sending message to the new end user!");
        //javaMailSender.send(mailMessage);
    }

    @Override
    public void newTicket(User currentUser, String ticketName) {
        var mailMessage = new SimpleMailMessage();

        mailMessage.setTo(currentUser.getEmail());
        mailMessage.setSubject("Ticket created");
        mailMessage.setText("Hello "+currentUser.getFirstName()+
                "\n\nYou have created a Ticket: "+ticketName+"\n\n"+
                "An agent will reply soon to provide assistance.\n\n"+
                "Please contact us again if you need further assistance."
        );
        log.debug("Sending message to creator!");
        //javaMailSender.send(mailMessage);
    }

    @Override
    public void newTicketWithUsername(User assignedTo, String ticketName) {
        var mailMessage = new SimpleMailMessage();

        mailMessage.setTo(assignedTo.getEmail());
        mailMessage.setSubject("You have been assigned to a new ticket");
        mailMessage.setText("Hello "+assignedTo.getFirstName()+
                "\n\nYou have been assigned to ticket: "+ticketName+"\n\n"+
                "Please log in to view updates."
        );
        log.debug("Creator assigned a user during ticket creation. Sending a message to the assigned to!");
        //javaMailSender.send(mailMessage);
    }

    @Override
    public void messageToCreator(User creator, String ticketName, Long ticketId) {
        var mailMessage = new SimpleMailMessage();

        mailMessage.setTo(creator.getEmail());
        mailMessage.setSubject("Your Ticket "+ticketId+" was updated");
        mailMessage.setText("Hello "+creator.getFirstName()+
                "\n\nYour ticket: "+ticketName+"\n\n"+
                "Has been updated in the system.\n\n"+
                "Please log in to view updates."
        );
        log.debug("Sending message to creator!");
        //javaMailSender.send(mailMessage);
    }

    @Override
    public void messageToAssignedTo(User assignedTo, String ticketName, Long ticketId) {
        var mailMessage = new SimpleMailMessage();

        mailMessage.setTo(assignedTo.getEmail());
        mailMessage.setSubject("You have been assigned to "+ticketId);
        mailMessage.setText("Hello "+assignedTo.getFirstName()+
                "\n\nYou have been assigned to ticket: "+ticketName+"\n\n"+
                "Please log in to view updates."
        );
        log.debug("Sending message to the assigned to!");
        //javaMailSender.send(mailMessage);
    }

    @Override
    public void messageFromCreatorToAssignedTo(User assignedTo, String ticketName, Long ticketId) {
        var mailMessage = new SimpleMailMessage();

        mailMessage.setTo(assignedTo.getEmail());
        mailMessage.setSubject("The Creator has replied to ticket "+ticketId);
        mailMessage.setText("Hello "+assignedTo.getFirstName()+
                "\n\nAs you are assigned to: "+ticketName+"\n\n"+
                "Please log in to view updates from the creator."
        );
        log.debug("Sending message from the creator to the assigned to!");
        //javaMailSender.send(mailMessage);
    }
}
