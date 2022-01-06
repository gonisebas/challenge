package com.tenpo.challenge.security;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {

    private static final String JWT_SECRET = "secret";
    private static final int JWT_EXPIRATION_MS = Integer.MAX_VALUE;

    JwtUtils jwtUtils;

    @BeforeEach
    public void setup(){
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", JWT_SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", JWT_EXPIRATION_MS);
    }

    @Test
    public void generateJwtTokenOk(){
        var now = new Date();
        var authentication = mock(Authentication.class);
        var userDetails = new UserDetailsImpl("user@tenpo.mail", "pdw", null);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        var result = jwtUtils.generateJwtToken(authentication);

        assertNotNull(result);
        assertEquals("user@tenpo.mail", getSubject(result));
        assertTrue(getExpiration(result).after(now));
    }

    private String getSubject(String token ){
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    private Date getExpiration(String token ){
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().getExpiration();
    }
}
