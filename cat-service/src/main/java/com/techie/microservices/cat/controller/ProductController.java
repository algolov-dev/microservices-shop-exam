package com.techie.microservices.cat.controller;

import com.techie.microservices.cat.model.City;
import com.techie.microservices.cat.model.Image;
import com.techie.microservices.cat.model.Product;
import com.techie.microservices.cat.model.User;
import com.techie.microservices.cat.service.CityService;
import com.techie.microservices.cat.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CityService cityService;

    @GetMapping("/")
    public String products(
            @RequestParam(name = "searchWord", required = false) String search_title,
            @RequestParam(name = "searchCity", required = false) String search_city,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            Principal principal,
            Model model) {

        Page<Product> productPage = productService.listProducts(search_title, search_city, PageRequest.of(page, size));
        List<City> cities = cityService.getAllCities();

        model.addAttribute("productPage", productPage);
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("searchWord", search_title != null ? search_title : "");
        model.addAttribute("searchCity", search_city != null ? search_city : "");
        model.addAttribute("cities", cities);

        return "products";
    }

    @GetMapping("/product/{id}")
    public String productInfo(@PathVariable Long id, Model model, Principal principal) {
        Product product = productService.getProductById(id);
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("product", product);
        model.addAttribute("authorProduct", product.getUser());

        List<Image> images = product.getImages();
        model.addAttribute("images", images);
        for (int i = 0; i < images.size(); i++) {
            model.addAttribute("image" + i, images.get(i));
            model.addAttribute("previewImageIndex"+i, images.get(i).isPreviewImage());
        }
        model.addAttribute("imagesCount", images.size());


        return "product-info";
    }

    @GetMapping("/product/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model, Principal principal) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        return "product-edit";
    }

    @PostMapping("/product/create")
    public String createProduct(
            Principal principal,
            Product product,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "searchCity", required = false) String searchCity,
            Model model
    ) {
        try {
            log.info("Продукт при сохранении: {}",product.getTitle());
            if (searchCity != null && !searchCity.isEmpty()) {
                product.setCity(searchCity);
            }
            log.info("Город после set: {}",product.getCity());

            productService.saveProduct(principal, product, files);
            return "redirect:/my/products";
        } catch (IllegalArgumentException | IOException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/products?error=" + e.getMessage();
        }
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, Principal principal) {
        log.info("Attempting to delete product with id: {}", id);
        productService.deleteProduct(productService.getUserByPrincipal(principal), id);
        log.info("Product with id: {} deleted by user: {}", id, productService.getUserByPrincipal(principal).getEmail());
        return "redirect:/my/products";
    }

    @GetMapping("/my/products")
    public String userProducts(@RequestParam(name = "searchCity", required = false) String search_city,
                               Principal principal,
                               Model model) {
        User user = productService.getUserByPrincipal(principal);

        List<City> cities = cityService.getAllCities();

        model.addAttribute("user", user);
        model.addAttribute("products", user.getProducts());
        model.addAttribute("searchCity", search_city != null ? search_city : "");
        model.addAttribute("cities", cities);
        return "my-products";
    }
}
