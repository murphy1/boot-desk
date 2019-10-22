package com.murphy1.serviced.serviced.repositories;

import com.murphy1.serviced.serviced.model.ResetToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<ResetToken, Long> {

    Optional<ResetToken> findByToken(String token);

}
