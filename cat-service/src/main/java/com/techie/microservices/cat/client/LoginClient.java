package com.techie.microservices.cat.client;

import groovy.util.logging.Slf4j;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

@Slf4j
public interface LoginClient {

    Logger log = LoggerFactory.getLogger(LoginClient.class);

    @GetExchange("/api/login")
    @CircuitBreaker(name = "login", fallbackMethod = "fallbackMethod")
    @Retry(name = "login")
    boolean isInStock(@RequestParam String email, @RequestParam String password);

    default boolean fallbackMethod(String email, String password, Throwable throwable) {
        log.info("Cannot login for user {}, failure reason: {}", email, throwable.getMessage());
        return false;
    }
}
