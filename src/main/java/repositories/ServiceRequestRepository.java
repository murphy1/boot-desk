package repositories;

import model.ServiceRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRequestRepository extends CrudRepository<ServiceRequest, Long> {
}
