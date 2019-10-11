package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.model.EndUser;
import com.murphy1.serviced.serviced.model.User;
import com.murphy1.serviced.serviced.repositories.EndUserRepository;
import com.murphy1.serviced.serviced.services.EndUserService;
import com.murphy1.serviced.serviced.services.UserService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.mail.SimpleMailMessage;

@Service
public class EndUserServiceImpl implements EndUserService {

    private EndUserRepository endUserRepository;
    private UserService userService;

    private JavaMailSender javaMailSender;

    public EndUserServiceImpl(EndUserRepository endUserRepository, UserService userService, JavaMailSender javaMailSender) {
        this.endUserRepository = endUserRepository;
        this.userService = userService;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public List<EndUser> getAllEndUsers() {

        List<EndUser> endUsers = new ArrayList<>();

        endUserRepository.findAll().iterator().forEachRemaining(endUsers :: add);

        return endUsers;
    }

    @Override
    public EndUser saveEndUser(EndUser endUser) {

        if (!endUser.getPassword().equals(endUser.getPasswordCheck())){
            throw new RuntimeException("Passwords must match!");
        }

        if (!endUser.getEmail().equals(endUser.getEmailCheck())){
            throw new RuntimeException("Emails must match!");
        }

        List<User> users = userService.getAllUsers();
        for (User user : users){
            if (user.getUsername().equalsIgnoreCase(endUser.getUsername())){
                throw new RuntimeException("Username is already taken!");
            }
            else if (user.getEmail().equalsIgnoreCase(endUser.getEmail())){
                throw new RuntimeException("An account already exists with this email!");
            }
        }

        if (endUser.getId() == null){

            endUser.setActive(true);
            endUser.setRoles("END_USER");

            var mailMessage = new SimpleMailMessage();

            mailMessage.setTo(endUser.getEmail());
            mailMessage.setSubject("Thank you for registering!");
            mailMessage.setText("Hello "+endUser.getFirstName()+
                    " and welcome to Boot Desk!\n\n" +
                            "You can now open Tickets.\n\n"+
                            "Please contact your admin if you require anything other than End User access."
                    );

            //javaMailSender.send(mailMessage);

        }

        return endUserRepository.save(endUser);
    }

    @Override
    public void deleteEndUser(Long id) {
        endUserRepository.deleteById(id);
    }

    @Override
    public EndUser findEndUserById(Long id) {

        Optional<EndUser> optionalEndUser = endUserRepository.findById(id);

        if (optionalEndUser.isEmpty()){
            throw new RuntimeException("End user id does not exist");
        }

        return optionalEndUser.get();
    }
}
