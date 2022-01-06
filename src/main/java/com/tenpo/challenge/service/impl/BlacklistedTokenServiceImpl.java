package com.tenpo.challenge.service.impl;

import com.tenpo.challenge.redis.BlacklistedTokenRepository;
import com.tenpo.challenge.service.BlacklistedTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlacklistedTokenServiceImpl implements BlacklistedTokenService {

    @Autowired
    BlacklistedTokenRepository blacklistedTokenRepository;

    @Override
    public boolean existsToken(String jwt) {
        return !blacklistedTokenRepository.findByToken(jwt).isEmpty();
    }
}
