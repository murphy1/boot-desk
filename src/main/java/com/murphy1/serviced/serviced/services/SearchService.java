package com.murphy1.serviced.serviced.services;

import com.murphy1.serviced.serviced.model.Ticket;

import java.util.List;

public interface SearchService {
    List<Ticket> issueResult(String object, String query);
    List<Ticket> issueResult(String object, String object1, String query, String query1);
    List<Ticket> serviceRequestResult(String object, String query);
    List<Ticket> serviceRequestResult(String object, String query, String object1, String query1);
    List<Ticket> search(List<Ticket> listToSearch, String object, String query);
}
