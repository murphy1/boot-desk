package com.murphy1.serviced.serviced.services;

import com.murphy1.serviced.serviced.model.Agent;

import java.util.List;

public interface AgentService {
    List<Agent> getAllAgents();
    Agent saveAgent(Agent agent);
    void deleteAgent(Long id);
    Agent findAgentById(Long id);
}
