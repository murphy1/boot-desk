package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.exceptions.BadRequestException;
import com.murphy1.serviced.serviced.exceptions.NotFoundException;
import com.murphy1.serviced.serviced.model.EndUser;
import com.murphy1.serviced.serviced.model.User;
import com.murphy1.serviced.serviced.repositories.EndUserRepository;
import com.murphy1.serviced.serviced.services.EndUserService;
import com.murphy1.serviced.serviced.services.MailService;
import com.murphy1.serviced.serviced.services.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EndUserServiceImpl implements EndUserService {

    private EndUserRepository endUserRepository;
    private UserService userService;

    private MailService mailService;

    public EndUserServiceImpl(EndUserRepository endUserRepository, UserService userService, MailService mailService) {
        this.endUserRepository = endUserRepository;
        this.userService = userService;
        this.mailService = mailService;
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
            throw new BadRequestException("Passwords must match!");
        }

        if (!endUser.getEmail().equals(endUser.getEmailCheck())){
            throw new BadRequestException("Emails must match!");
        }

        if (endUser.getId() == null){

            List<User> users = userService.getAllUsers();
            for (User user : users){
                if (user.getUsername().equalsIgnoreCase(endUser.getUsername())){
                    throw new BadRequestException("Username is already taken!");
                }
                else if (user.getEmail().equalsIgnoreCase(endUser.getEmail())){
                    throw new BadRequestException("An account already exists with this email!");
                }
            }
            endUser.setActive(true);

            // Send welcome message to the new user
            mailService.newUser(endUser);
        }


        endUser.setRoles("END_USER");

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
            throw new NotFoundException("End user id does not exist");
        }

        return optionalEndUser.get();
    }
}
