package com.murphy1.serviced.serviced.repositories;

import com.murphy1.serviced.serviced.model.ServiceRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRequestRepository extends CrudRepository<ServiceRequest, Long> {
}
