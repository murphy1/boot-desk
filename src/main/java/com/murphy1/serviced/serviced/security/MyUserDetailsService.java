package com.murphy1.serviced.serviced.security;

import com.murphy1.serviced.serviced.model.Admin;
import com.murphy1.serviced.serviced.model.Agent;
import com.murphy1.serviced.serviced.model.EndUser;
import com.murphy1.serviced.serviced.repositories.AdminRepository;
import com.murphy1.serviced.serviced.repositories.AgentRepository;
import com.murphy1.serviced.serviced.repositories.EndUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Primary
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private EndUserRepository endUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Admin> adminOptional = adminRepository.findByUsername(username);
        if (!adminOptional.isPresent()){
            Optional<Agent> agentOptional = agentRepository.findByUsername(username);
            if (!agentOptional.isPresent()){
                Optional<EndUser> endUserOptional = endUserRepository.findByUsername(username);
                if (!endUserOptional.isPresent()){
                    throw new RuntimeException("No users exist with that username!");
                }
                return endUserOptional.map(MyUserDetails::new).get();
            }
            return agentOptional.map(MyUserDetails::new).get();
        }
        return adminOptional.map(MyUserDetails::new).get();
    }
}
