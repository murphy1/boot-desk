package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.model.Admin;
import com.murphy1.serviced.serviced.model.Agent;
import com.murphy1.serviced.serviced.model.EndUser;
import com.murphy1.serviced.serviced.model.User;
import com.murphy1.serviced.serviced.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public Agent convertUserToAgent(User user) {
        Agent agent = new Agent();

        agent.setId(user.getId());
        agent.setFirstName(user.getFirstName());
        agent.setLastName(user.getLastName());
        agent.setUsername(user.getUsername());
        agent.setPassword(user.getPassword());

        return agent;
    }

    @Override
    public EndUser convertUserToEndUser(User user) {
        EndUser endUser = new EndUser();

        endUser.setId(user.getId());
        endUser.setFirstName(user.getFirstName());
        endUser.setLastName(user.getLastName());
        endUser.setUsername(user.getUsername());
        endUser.setPassword(user.getPassword());

        return endUser;
    }

    @Override
    public Admin convertUserToAdmin(User user) {
        Admin admin = new Admin();

        admin.setId(user.getId());
        admin.setFirstName(user.getFirstName());
        admin.setLastName(user.getLastName());
        admin.setUsername(user.getUsername());
        admin.setPassword(user.getPassword());

        return admin;
    }

    public String getRole(String username){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<String> role = authentication.getAuthorities().stream()
                .map(roles -> ((GrantedAuthority) roles).getAuthority())
                .findFirst();

        return role.get();
    }

    @Override
    public String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        return username;
    }
}
