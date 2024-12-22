package com.techie.microservices.cat.controller;

import com.techie.microservices.cat.client.LoginClient;
import com.techie.microservices.cat.dto.CatRequest;
import com.techie.microservices.cat.model.Product;
import com.techie.microservices.cat.model.User;
import com.techie.microservices.cat.service.CatService;
import com.techie.microservices.cat.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CatController {

    private final CatService catService;
    private final ProductService productService;
    private final LoginClient loginClient;

    @PostMapping("/cat/create")
    public String createCat(@RequestParam Long productId, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = productService.getUserByPrincipal(principal);
            Product product = productService.getProductById(productId);

            var userIsAuth = loginClient.isInStock("login", "password");
            if (currentUser == null || !currentUser.isActive()) {
                redirectAttributes.addFlashAttribute("error", "Пользователь не активен или не авторизован");
                return "redirect:/product/" + productId;
            }

            CatRequest catRequest = new CatRequest(
                    product.getTitle(),
                    product.getPrice(),
                    currentUser.getEmail(),
                    product.getUser().getEmail()
            );

            catService.placeCat(catRequest);
            redirectAttributes.addFlashAttribute("success", "Заказ успешно оформлен");
            return "redirect:/product/" + productId;
        } catch (Exception e) {
            log.error("Ошибка при создании заказа: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Произошла ошибка при оформлении заказа: " + e.getMessage());
            return "redirect:/product/" + productId;
        }
    }


}

