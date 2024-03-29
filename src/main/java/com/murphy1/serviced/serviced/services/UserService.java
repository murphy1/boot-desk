package com.murphy1.serviced.serviced.services;

import com.murphy1.serviced.serviced.model.Admin;
import com.murphy1.serviced.serviced.model.Agent;
import com.murphy1.serviced.serviced.model.EndUser;
import com.murphy1.serviced.serviced.model.User;

import java.util.List;

public interface UserService {
    Agent convertUserToAgent(User user);
    User convertAgentToUser(Agent agent);
    EndUser convertUserToEndUser(User user);
    User convertEndUserToUser(EndUser endUser);
    Admin convertUserToAdmin(User user);
    User convertAdminToUser(Admin admin);
    String getRole(String username);
    String getCurrentUserName();
    List<User> getAllUsers();
    User findUserByUsername(String username);
    User findUserByEmail(String email);
    void changeToEndUser(User user);
    void changeToAgent(User user);
    void changeToAdmin(User user);
}
