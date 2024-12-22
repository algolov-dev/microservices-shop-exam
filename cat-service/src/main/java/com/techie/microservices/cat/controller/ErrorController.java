package com.techie.microservices.cat.controller;

import com.techie.microservices.cat.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("user", new User());
        return "access-denied";
    }
}
