package com.murphy1.serviced.serviced.repositories;

import com.murphy1.serviced.serviced.model.Agent;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AgentRepository extends CrudRepository<Agent, Long> {

    Optional<Agent> findByUsername(String username);

}
