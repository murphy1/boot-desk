package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.model.User;
import org.springframework.stereotype.Service;
import com.murphy1.serviced.serviced.repositories.UserRepository;
import com.murphy1.serviced.serviced.services.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        userRepository.findAll().iterator().forEachRemaining(users :: add);

        return users;
    }
}
