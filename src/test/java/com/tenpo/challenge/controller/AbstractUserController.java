package com.tenpo.challenge.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import redis.embedded.RedisServer;

public abstract class AbstractUserController {

    private static RedisServer redisServer;

    @BeforeAll
    public static void setUp() {
        try {
            redisServer = RedisServer.builder().port(6370).build();
            redisServer.start();
        } catch (Exception e) {
            //do nothing
        }
    }

    @AfterAll
    public static void tearDown() {
        redisServer.stop();
    }

}
