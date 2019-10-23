package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.exceptions.BadRequestException;
import com.murphy1.serviced.serviced.exceptions.NotFoundException;
import com.murphy1.serviced.serviced.model.Admin;
import com.murphy1.serviced.serviced.model.Agent;
import com.murphy1.serviced.serviced.model.EndUser;
import com.murphy1.serviced.serviced.model.User;
import com.murphy1.serviced.serviced.repositories.AdminRepository;
import com.murphy1.serviced.serviced.repositories.AgentRepository;
import com.murphy1.serviced.serviced.repositories.EndUserRepository;
import com.murphy1.serviced.serviced.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private AdminRepository adminRepository;
    private AgentRepository agentRepository;
    private EndUserRepository endUserRepository;

    public UserServiceImpl(AdminRepository adminRepository, AgentRepository agentRepository, EndUserRepository endUserRepository) {
        this.adminRepository = adminRepository;
        this.agentRepository = agentRepository;
        this.endUserRepository = endUserRepository;
    }

    @Override
    public Agent convertUserToAgent(User user) {
        Agent agent = new Agent();

        agent.setId(user.getId());
        agent.setFirstName(user.getFirstName());
        agent.setLastName(user.getLastName());
        agent.setUsername(user.getUsername());
        agent.setPassword(user.getPassword());
        agent.setPasswordCheck(user.getPassword());
        agent.setEmail(user.getEmail());
        agent.setEmailCheck(user.getEmail());
        agent.setRoles(user.getRoles());
        if (user.isActive()){
            agent.setActive(true);
        }

        return agent;
    }

    @Override
    public User convertAgentToUser(Agent agent) {
        User user = new User();

        user.setId(agent.getId());
        user.setFirstName(agent.getFirstName());
        user.setLastName(agent.getLastName());
        user.setPassword(agent.getPassword());
        user.setEmail(agent.getEmail());
        user.setRoles(agent.getRoles());

        return user;
    }

    @Override
    public EndUser convertUserToEndUser(User user) {
        EndUser endUser = new EndUser();

        endUser.setId(user.getId());
        endUser.setFirstName(user.getFirstName());
        endUser.setLastName(user.getLastName());
        endUser.setUsername(user.getUsername());
        endUser.setPassword(user.getPassword());
        endUser.setPasswordCheck(user.getPasswordCheck());
        endUser.setEmail(user.getEmail());
        endUser.setEmailCheck(user.getEmailCheck());
        if (user.isActive()){
            endUser.setActive(true);
        }

        return endUser;
    }

    @Override
    public User convertEndUserToUser(EndUser endUser) {
        User user = new User();

        user.setId(endUser.getId());
        user.setFirstName(endUser.getFirstName());
        user.setLastName(endUser.getLastName());
        user.setPassword(endUser.getPassword());
        user.setEmail(endUser.getEmail());
        user.setRoles(endUser.getRoles());

        return user;
    }

    @Override
    public Admin convertUserToAdmin(User user) {
        Admin admin = new Admin();

        admin.setId(user.getId());
        admin.setFirstName(user.getFirstName());
        admin.setLastName(user.getLastName());
        admin.setUsername(user.getUsername());
        admin.setPassword(user.getPassword());
        admin.setPasswordCheck(user.getPassword());
        admin.setEmail(user.getEmail());
        admin.setEmailCheck(user.getEmail());
        if (user.isActive()){
            admin.setActive(true);
        }
        admin.setRoles(user.getRoles());

        return admin;
    }

    @Override
    public User convertAdminToUser(Admin admin) {
        User user = new User();

        user.setId(admin.getId());
        user.setFirstName(admin.getFirstName());
        user.setLastName(admin.getLastName());
        user.setPassword(admin.getPassword());
        user.setEmail(admin.getEmail());
        user.setRoles(admin.getRoles());

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
        if (principal.equals("anonymousUser")){
            return "ANON";
        }
        String username = ((UserDetails)principal).getUsername();
        return username;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        adminRepository.findAll().iterator().forEachRemaining(users::add);
        agentRepository.findAll().iterator().forEachRemaining(users::add);
        endUserRepository.findAll().iterator().forEachRemaining(users::add);

        return users;
    }

    @Override
    public User findUserByUsername(String username) {
        List<User> users = getAllUsers();

        for (User user : users){
            if (user.getUsername().equals(username)){
                return user;
            }
        }
        log.error("Username does not exist.  UserServiceImpl");
        throw new NotFoundException("Username does not exist! LOGGED");
    }

    @Override
    public User findUserByEmail(String email) {
        List<User> users = getAllUsers();

        for (User user : users){
            if (user.getEmail().equals(email)){
                return user;
            }
        }
        throw new NotFoundException("No user exists with that email");
    }

    @Override
    public void changeToEndUser(User user) {
        if (user.getRoles().equals("END_USER")){
            throw new BadRequestException("User is already an End User!");
        }

        EndUser endUser = new EndUser();
        endUser.setFirstName(user.getFirstName());
        endUser.setLastName(user.getLastName());
        endUser.setUsername(user.getUsername());
        endUser.setEmail(user.getEmail());
        endUser.setEmailCheck(user.getEmail());
        endUser.setPassword(user.getPassword());
        endUser.setRoles("END_USER");
        endUser.setActive(true);

        if (user.getRoles().equals("ADMIN")){
            adminRepository.delete(convertUserToAdmin(user));
        }
        else if (user.getRoles().equals("AGENT")){
            agentRepository.delete(convertUserToAgent(user));
        }

        endUserRepository.save(endUser);
    }

    @Override
    public void changeToAgent(User user) {
        if (user.getRoles().equals("AGENT")){
            throw new BadRequestException("User is already an Agent!");
        }

        Agent agent = new Agent();
        agent.setFirstName(user.getFirstName());
        agent.setLastName(user.getLastName());
        agent.setUsername(user.getUsername());
        agent.setEmail(user.getEmail());
        agent.setEmailCheck(user.getEmail());
        agent.setPassword(user.getPassword());
        agent.setRoles("AGENT");
        agent.setActive(true);

        if (user.getRoles().equals("END_USER")){
            endUserRepository.delete(convertUserToEndUser(user));
        }
        else if (user.getRoles().equals("ADMIN")){
            adminRepository.delete(convertUserToAdmin(user));
        }

        agentRepository.save(agent);
    }

    @Override
    public void changeToAdmin(User user) {
        if (user.getRoles().equals("ADMIN")){
            throw new BadRequestException("User is already an Admin!");
        }

        Admin admin = new Admin();
        admin.setFirstName(user.getFirstName());
        admin.setLastName(user.getLastName());
        admin.setUsername(user.getUsername());
        admin.setEmail(user.getEmail());
        admin.setEmailCheck(user.getEmail());
        admin.setPassword(user.getPassword());
        admin.setRoles("ADMIN");
        admin.setActive(true);

        if (user.getRoles().equals("END_USER")){
            endUserRepository.delete(convertUserToEndUser(user));
        }
        else if (user.getRoles().equals("AGENT")){
            agentRepository.delete(convertUserToAgent(user));
        }

        adminRepository.save(admin);
    }
}
