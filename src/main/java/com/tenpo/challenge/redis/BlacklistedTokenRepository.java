package com.tenpo.challenge.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlacklistedTokenRepository  extends CrudRepository<BlacklistedToken, Long> {

    List<BlacklistedToken> findByToken(String token);

}

