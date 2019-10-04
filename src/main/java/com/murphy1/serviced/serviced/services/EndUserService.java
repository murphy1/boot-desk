package com.murphy1.serviced.serviced.services;

import com.murphy1.serviced.serviced.model.EndUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EndUserService{
    List<EndUser> getAllEndUsers();
    EndUser saveEndUser(EndUser endUser);
    void deleteEndUser(Long id);
    EndUser findEndUserById(Long id);
}
