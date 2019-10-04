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
