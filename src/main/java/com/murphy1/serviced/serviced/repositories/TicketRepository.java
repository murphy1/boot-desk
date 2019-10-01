package com.murphy1.serviced.serviced.repositories;

import com.murphy1.serviced.serviced.model.Ticket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {
}
