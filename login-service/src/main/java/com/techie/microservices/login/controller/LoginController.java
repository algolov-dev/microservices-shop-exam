package com.techie.microservices.login.controller;

import com.techie.microservices.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final LoginService loginService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@RequestParam String email, @RequestParam String password) {
        log.info("Received login request for email: {}", email);
        boolean result = loginService.isInStock(email, password);
        log.info("Login result for email {}: {}", email, result);
        return loginService.isInStock(email, password);
    }
}
