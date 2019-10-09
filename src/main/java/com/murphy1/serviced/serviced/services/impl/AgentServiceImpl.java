package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.model.Agent;
import com.murphy1.serviced.serviced.repositories.AgentRepository;
import com.murphy1.serviced.serviced.services.AgentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AgentServiceImpl implements AgentService {

    private AgentRepository agentRepository;

    public AgentServiceImpl(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
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
            throw new RuntimeException("Passwords must match!");
        }

        List<Agent> agents = getAllAgents();
        for (Agent agent1 : agents){
            if (agent.getUsername().equals(agent1.getUsername())){
                throw new RuntimeException("Username already exists!");
            }
        }

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
            throw new RuntimeException("Agent id does not exist");
        }

        return optionalAgent.get();
    }
}
