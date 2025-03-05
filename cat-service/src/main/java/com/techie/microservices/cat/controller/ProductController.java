package com.techie.microservices.cat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techie.microservices.cat.model.Category;
import com.techie.microservices.cat.model.Image;
import com.techie.microservices.cat.model.Product;
import com.techie.microservices.cat.model.User;
import com.techie.microservices.cat.service.CategoryService;
import com.techie.microservices.cat.service.ProductService;
import com.techie.microservices.cat.service.TagService;
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
import java.util.Arrays;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final TagService tagService;

    @GetMapping("/")
    public String products(
            @RequestParam(name = "searchWord", required = false) String searchTitle,
            @RequestParam(name = "searchCategory", required = false) String searchCategory,
            @RequestParam(name = "tag", required = false) String tag,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            Principal principal,
            Model model) {

        Page<Product> productPage;

        if (tag != null && !tag.trim().isEmpty()) {
            productPage = productService.listProductsByTag(tag, PageRequest.of(page, size));
        } else {
            productPage = productService.listProducts(searchTitle, searchCategory, PageRequest.of(page, size));
        }
        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("productPage", productPage);
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("searchWord", searchTitle != null ? searchTitle : "");
        model.addAttribute("searchCategory", searchCategory != null ? searchCategory : "");
        model.addAttribute("selectedTag", tag != null ? tag : "");
        model.addAttribute("categories", categories);
        model.addAttribute("topTags", tagService.getTopTags());

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
        User currentUser = productService.getUserByPrincipal(principal);

        if (currentUser == null || (!currentUser.isAdmin() && !product.getUser().getId().equals(currentUser.getId()))) {
            return "redirect:/access-denied";
        }

        if (product.getCategory() == null) {
            product.setCategory("");
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        return "product-edit";
    }

    @PostMapping("/product/update/{id}")
    public String updateProduct(
            @PathVariable Long id,
            Product product,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "tagNames", required = false) String tagNamesJson,
            @RequestParam(value = "category", required = false) String category,
            Principal principal,
            Model model) {
        try {
            List<String> tagNames = null;
            if (tagNamesJson != null && !tagNamesJson.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                tagNames = Arrays.asList(mapper.readValue(tagNamesJson, String[].class));
            }

            product.setId(id);
            if (category != null && !category.isEmpty()) {
                product.setCategory(category);
            }
            productService.updateProduct(principal, product, files, tagNames);
            return "redirect:/product/" +id;
        } catch (IllegalArgumentException | IOException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/product/edit/" + id + "?error=" + e.getMessage();
        }
    }


    @PostMapping("/product/create")
    public String createProduct(
            Principal principal,
            Product product,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "tagNames", required = false) String tagNamesJson,
            @RequestParam(value = "searchCategory", required = false) String searchCategory,
            Model model
    ) {
        try {
            List<String> tagNames = null;
            if (tagNamesJson != null && !tagNamesJson.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                tagNames = Arrays.asList(mapper.readValue(tagNamesJson, String[].class));
            }

            log.info("Список тегов, полученный из формы: {}", tagNames);

            if (searchCategory != null && !searchCategory.isEmpty()) {
                product.setCategory(searchCategory);
            }
            productService.saveProduct(principal, product, files, tagNames);
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
        return "redirect:/";
    }

    @GetMapping("/my/products")
    public String userProducts(@RequestParam(name = "searchCategory", required = false) String search_category,
                               Principal principal,
                               Model model) {
        User user = productService.getUserByPrincipal(principal);

        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("user", user);
        model.addAttribute("products", user.getProducts());
        model.addAttribute("searchCategory", search_category != null ? search_category : "");
        model.addAttribute("categories", categories);
        return "my-products";
    }

    @GetMapping("/product/deleteImage/{productId}/{imageId}")
    public String deleteImage(@PathVariable("productId") Long productId,
                              @PathVariable("imageId") Long imageId,
                              Principal principal) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            log.error("Продукт с ID {} не найден", productId);
            return "redirect:/my/products?error=Product not found";
        }

        User currentUser = productService.getUserByPrincipal(principal);
        if (!product.getUser().getId().equals(currentUser.getId())) {
            log.error("Пользователь {} не имеет права удалить изображение продукта {}",
                    currentUser.getEmail(), productId);
            return "redirect:/my/products?error=Not authorized";
        }

        if (product.getImages().size() <= 1) {
            log.warn("Попытка удалить единственное изображение для продукта с ID {}", productId);
            return "redirect:/product/edit/" + productId + "?error=Нельзя удалить единственное изображение";
        }

        productService.deleteImage(product, imageId);
        log.info("Изображение с ID {} удалено из продукта с ID {}", imageId, productId);

        return "redirect:/product/edit/" + productId;
    }
}
