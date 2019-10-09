package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.model.EndUser;
import com.murphy1.serviced.serviced.repositories.EndUserRepository;
import com.murphy1.serviced.serviced.services.EndUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EndUserServiceImpl implements EndUserService {

    private EndUserRepository endUserRepository;

    public EndUserServiceImpl(EndUserRepository endUserRepository) {
        this.endUserRepository = endUserRepository;
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

        List<EndUser> endUsers = getAllEndUsers();
        for (EndUser endUser1 : endUsers){
            if (endUser.getUsername().equals(endUser1.getUsername())){
                throw new RuntimeException("Username already exists!");
            }
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
