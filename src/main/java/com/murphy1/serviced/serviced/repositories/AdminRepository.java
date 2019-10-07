package com.murphy1.serviced.serviced.repositories;

import com.murphy1.serviced.serviced.model.Admin;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdminRepository extends CrudRepository<Admin, Long> {

    Optional<Admin> findByUsername(String username);

}
