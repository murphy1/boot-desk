package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.exceptions.BadRequestException;
import com.murphy1.serviced.serviced.exceptions.NotFoundException;
import com.murphy1.serviced.serviced.model.Agent;
import com.murphy1.serviced.serviced.model.User;
import com.murphy1.serviced.serviced.repositories.AgentRepository;
import com.murphy1.serviced.serviced.services.AgentService;
import com.murphy1.serviced.serviced.services.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AgentServiceImpl implements AgentService {

    private AgentRepository agentRepository;
    private UserService userService;

    public AgentServiceImpl(AgentRepository agentRepository, UserService userService) {
        this.agentRepository = agentRepository;
        this.userService = userService;
    }

    @Override
    public List<Agent> getAllAgents() {

        List<Agent> agents = new ArrayList<>();

        agentRepository.findAll().iterator().forEachRemaining(agents :: add);

        return agents;
    }

    @Override
    public Agent saveAgent(Agent agent) {

        if (!agent.getPassword().equals(agent.getPasswordCheck())){
            throw new BadRequestException("Passwords must match!");
        }

        if (!agent.getEmail().equals(agent.getEmailCheck())){
            throw new BadRequestException("Emails must match!");
        }

        if (agent.getId() == null){

            List<User> users = userService.getAllUsers();
            for (User user : users){
                if (user.getUsername().equalsIgnoreCase(agent.getUsername())){
                    throw new BadRequestException("Username is already taken!");
                }
                else if (user.getEmail().equalsIgnoreCase(agent.getEmail())){
                    throw new BadRequestException("An account already exists with this email!");
                }
            }

            agent.setActive(true);

        }

        agent.setRoles("AGENT");

        return agentRepository.save(agent);
    }

    @Override
    public void deleteAgent(Long id) {
        agentRepository.deleteById(id);
    }

    @Override
    public Agent findAgentById(Long id) {

        Optional<Agent> optionalAgent = agentRepository.findById(id);

        if (optionalAgent.isEmpty()){
            throw new NotFoundException("Agent id does not exist");
        }

        return optionalAgent.get();
    }

    @Override
    public Agent findAgentByUsername(String username) {
        Optional<Agent> agentOptional = agentRepository.findByUsername(username);

        if (!agentOptional.isPresent()){
            throw new NotFoundException("An Agent does not exist with that username!");
        }

        return agentOptional.get();
    }
}
