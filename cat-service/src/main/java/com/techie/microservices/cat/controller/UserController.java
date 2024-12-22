package com.techie.microservices.cat.controller;

import com.techie.microservices.cat.model.User;
import com.techie.microservices.cat.model.enums.Role;
import com.techie.microservices.cat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Collections;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Principal principal, Model model) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        if (error != null) {
            model.addAttribute("error", true);
        }
        return "login";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/registration")
    public String registration(Principal principal, Model model) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "registration";
    }


    @PostMapping("/registration")
    public String createUser(User user, Model model) {
        if (!userService.createUser(user)) {
            model.addAttribute("errorMessage", "Пользователь с email: " + user.getEmail() + " уже существует");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/user/{user}")
    public String userInfo(@PathVariable("user") User user, Model model, Principal principal) {
        model.addAttribute("user", user);
        model.addAttribute("userByPrincipal", userService.getUserByPrincipal(principal));
        model.addAttribute("products", user.getProducts());
        return "user-info";
    }

    @PostMapping("/cronjob2")
    public String createUserByCronJob(Model model) {
        log.info("ДОШЛО!");
        User newUser = new User();
        newUser.setEmail("cronjob@example.com");
        newUser.setPhone_number("1234567890");
        newUser.setName("CronJob User");
        newUser.setActive(true);
        newUser.setPassword("password123");
        newUser.setRoles(Collections.singleton(Role.ROLE_USER));

        if (!userService.createUser(newUser)) {
            model.addAttribute("errorMessage", "Пользователь с email: " + newUser.getEmail() + " уже существует");
            return "error";
        }

        model.addAttribute("successMessage", "Пользователь успешно создан через CronJob");
        return "success";
    }


}
