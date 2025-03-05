package com.techie.microservices.cat.controller;

import com.techie.microservices.cat.model.User;
import com.techie.microservices.cat.model.Tag;
import com.techie.microservices.cat.model.Category;
import com.techie.microservices.cat.model.enums.Role;
import com.techie.microservices.cat.service.UserService;
import com.techie.microservices.cat.service.TagService;
import com.techie.microservices.cat.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserService userService;
    private final TagService tagService;
    private final CategoryService categoryService;

    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        model.addAttribute("users", userService.list());
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        // Для отображения в админ панели (шаблон выше использует переменные tags и categories)
        model.addAttribute("tags", tagService.listAll());
        model.addAttribute("categories", categoryService.listAll());
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

    // ------------------ Работа с тегами ------------------

    @PostMapping("/admin/tags/add")
    public String addTag(@RequestParam("name") String name) {
        try {
            Tag tag = new Tag();
            tag.setName(name);
            tagService.save(tag);
            log.info("Тег '{}' успешно добавлен", name);
        } catch (Exception e) {
            log.error("Ошибка при добавлении тега '{}'", name, e);
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/tags/edit/{tag}")
    public String editTag(@PathVariable("tag") Tag tag, Model model) {
        model.addAttribute("tag", tag);
        return "tag-edit"; // Возвращает страницу редактирования тега
    }

    @PostMapping("/admin/tags/edit")
    public String updateTag(@RequestParam("tagId") Tag tag,
                            @RequestParam("name") String name) {
        try {
            tag.setName(name);
            tagService.save(tag);
            log.info("Тег с ID {} успешно обновлен", tag.getId());
        } catch (Exception e) {
            log.error("Ошибка при обновлении тега с ID {}", tag.getId(), e);
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/tags/delete/{id}")
    public String deleteTag(@PathVariable("id") Long id) {
        try {
            tagService.deleteById(id);
            log.info("Тег с ID {} удален", id);
        } catch (Exception e) {
            log.error("Ошибка при удалении тега с ID {}", id, e);
        }
        return "redirect:/admin";
    }

    // ------------------ Работа с категориями ------------------

    @PostMapping("/admin/categories/add")
    public String addCategory(@RequestParam("category_name") String categoryName) {
        try {
            Category category = new Category();
            category.setCategory_name(categoryName);
            categoryService.save(category);
            log.info("Категория '{}' успешно добавлена", categoryName);
        } catch (Exception e) {
            log.error("Ошибка при добавлении категории '{}'", categoryName, e);
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/categories/edit/{category}")
    public String editCategory(@PathVariable("category") Category category, Model model) {
        model.addAttribute("category", category);
        return "category-edit"; // Страница редактирования категории
    }

    @PostMapping("/admin/categories/edit")
    public String updateCategory(@RequestParam("categoryId") Category category,
                                 @RequestParam("category_name") String categoryName) {
        try {
            category.setCategory_name(categoryName);
            categoryService.save(category);
            log.info("Категория с ID {} успешно обновлена", category.getId());
        } catch (Exception e) {
            log.error("Ошибка при обновлении категории с ID {}", category.getId(), e);
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        try {
            categoryService.deleteById(id);
            log.info("Категория с ID {} удалена", id);
        } catch (Exception e) {
            log.error("Ошибка при удалении категории с ID {}", id, e);
        }
        return "redirect:/admin";
    }
}
