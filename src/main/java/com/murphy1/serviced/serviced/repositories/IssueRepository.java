package com.murphy1.serviced.serviced.repositories;

import com.murphy1.serviced.serviced.model.Issue;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends CrudRepository<Issue, Long> {
}
