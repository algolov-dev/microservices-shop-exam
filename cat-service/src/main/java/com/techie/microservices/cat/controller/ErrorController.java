package com.techie.microservices.cat.controller;

import com.techie.microservices.cat.model.User;
import com.techie.microservices.cat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ErrorController {

    private final UserService userService;

    @GetMapping("/access-denied")
    public String accessDenied(Model model, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user != null ? user : new User());
        return "access-denied";
    }
}
