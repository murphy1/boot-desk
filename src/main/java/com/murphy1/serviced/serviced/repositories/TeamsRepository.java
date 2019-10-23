package com.murphy1.serviced.serviced.repositories;

import com.murphy1.serviced.serviced.model.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamsRepository extends CrudRepository<Team, Long> {
}
