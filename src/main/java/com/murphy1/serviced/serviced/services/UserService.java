package com.murphy1.serviced.serviced.services;

import com.murphy1.serviced.serviced.model.Admin;
import com.murphy1.serviced.serviced.model.Agent;
import com.murphy1.serviced.serviced.model.EndUser;
import com.murphy1.serviced.serviced.model.User;

public interface UserService {

    Agent convertUserToAgent(User user);
    EndUser convertUserToEndUser(User user);
    Admin convertUserToAdmin(User user);
    String getRole(String username);

}
