package com.tenpo.challenge.converter;

import com.tenpo.challenge.redis.BlacklistedToken;
import com.tenpo.challenge.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtConverter {

    @Autowired
    JwtUtils jwtUtils;

    public BlacklistedToken convertToBlacklistedToken(final String authorizationToken) {
        var jwtToken = cleanJwtToken(authorizationToken);
        BlacklistedToken blacklistedToken = new BlacklistedToken();
        var expiration = jwtUtils.getExpiration(jwtToken);
        long diffInSeconds = Math.abs(expiration.getTime() - (new Date()).getTime())/1000;
        blacklistedToken.setExpirationSeconds(diffInSeconds);
        blacklistedToken.setToken(jwtToken);
        return blacklistedToken;
    }

    private String cleanJwtToken(String jwtToken) {
        return jwtToken.replace("Bearer", "").trim();
    }
}
