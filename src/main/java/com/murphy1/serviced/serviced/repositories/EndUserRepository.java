package com.murphy1.serviced.serviced.repositories;

import com.murphy1.serviced.serviced.model.EndUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EndUserRepository extends CrudRepository<EndUser, Long> {

    Optional<EndUser> findByUsername(String username);

}
