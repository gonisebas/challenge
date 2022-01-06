package com.tenpo.challenge.service;

public interface BlacklistedTokenService {

    boolean existsToken(String jwt);

}
