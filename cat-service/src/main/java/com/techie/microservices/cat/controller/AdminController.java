package com.techie.microservices.cat.controller;

import com.techie.microservices.cat.model.User;
import com.techie.microservices.cat.model.enums.Role;
import com.techie.microservices.cat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserService userService;

    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        model.addAttribute("users", userService.list());
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "admin";
    }

    @PostMapping("/admin/user/ban/{id}")
    public String userBan(@PathVariable("id") Long id) {
        userService.banUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/user/edit/{user}")
    public String userEdit(@PathVariable("user") User user, Model model, Principal principal) {
        model.addAttribute("user", user);
        model.addAttribute("userByPrincipal", userService.getUserByPrincipal(principal));
        model.addAttribute("roles", Role.values());
        return "user-edit";
    }

    @PostMapping("/admin/user/edit")
    public String userEdit(@RequestParam("userId") User user,
                           @RequestParam Map<String, String> form) {
        log.info("Начало метода userEdit для пользователя с ID: {}", user.getId());

        log.info("Полученные данные пользователя: имя = {}, телефон = {}",
                user.getName(), user.getPhone_number());
        log.info("Полученные данные формы: {}", form);

        try {


            String newPhoneNumber = form.get("phone_number");
            String newName = form.get("name");

            log.info("Form phone: {}", newPhoneNumber);
            log.info("Form name: {}", newName);

            userService.updateUser(user, newPhoneNumber, newName, form);

            log.info("Пользователь с ID: {} успешно обновлен.", user.getId());
        } catch (Exception e) {
            log.error("Ошибка при обновлении пользователя с ID: {}", user.getId(), e);
            return "redirect:/admin/error";
        }

        log.debug("Перенаправление на /admin после обновления пользователя с ID: {}", user.getId());
        return "redirect:/admin";
    }

}
