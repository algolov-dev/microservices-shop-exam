package com.techie.microservices.cat.service;

import com.techie.microservices.cat.model.Image;
import com.techie.microservices.cat.model.Product;
import com.techie.microservices.cat.model.User;
import com.techie.microservices.cat.repository.ProductRepository;
import com.techie.microservices.cat.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public Page<Product> listProducts(String title, String city, Pageable pageable) {
        boolean hasTitle = title != null && !title.trim().isEmpty();
        boolean hasCity = city != null && !city.trim().isEmpty();

        if (!hasTitle && !hasCity) {
            return productRepository.findAllByUser_ActiveTrue(pageable);
        } else if (hasTitle && !hasCity) {
            return productRepository.findByTitleContainingIgnoreCaseAndUser_ActiveTrue(title, pageable);
        } else if (!hasTitle && hasCity) {
            return productRepository.findByCityContainingIgnoreCaseAndUser_ActiveTrue(city, pageable);
        } else {
            return productRepository.findByTitleContainingIgnoreCaseAndCityContainingIgnoreCaseAndUser_ActiveTrue(title, city, pageable);
        }
    }


    public void saveProduct(Principal principal, Product product, List<MultipartFile> files) throws IOException {
        User user = getUserByPrincipal(principal);
        product.setUser(user);

        if (files != null && !files.isEmpty()) {
            if (files.size() > 10) {
                throw new IllegalArgumentException("Нельзя загрузить более 10 фотографий.");
            }

            boolean isFirst = true;
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue; // Пропускаем пустые файлы
                }
                Image image = toImageEntity(file);
                if (isFirst) {
                    image.setPreviewImage(true);
                    isFirst = false;
                }
                product.addImageToProduct(image);
            }

            if (product.getImages().isEmpty()) {
                throw new IllegalArgumentException("Пожалуйста, загрузите хотя бы одну фотографию.");
            }
        } else {
            throw new IllegalArgumentException("Пожалуйста, загрузите хотя бы одну фотографию.");
        }

        Product savedProduct = productRepository.save(product);

        if (!savedProduct.getImages().isEmpty()) {
            savedProduct.setPreviewImageId(savedProduct.getImages().get(0).getId());
            productRepository.save(savedProduct);
        } else {
            log.warn("No images were uploaded for the product: {}", product.getTitle());
        }
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();   // Для Freemarker
        return userRepository.findByEmail(principal.getName());
    }

    private Image toImageEntity(MultipartFile file1) throws IOException {
        Image image = new Image();
        image.setName(file1.getName());
        image.setOriginalFileName(file1.getOriginalFilename());
        image.setContentType(file1.getContentType());
        image.setSize(file1.getSize());
        image.setBytes(file1.getBytes());
        return image;
    }

    @Transactional
    public void deleteProduct(User user, Long id) {
        Product product = productRepository.findById(id)
                .orElse(null);
        if (product != null) {
            if (product.getUser().getId().equals(user.getId())) {
                user.getProducts().remove(product);
                product.setUser(null);
                productRepository.delete(product);
                log.info("Product with id = {} was deleted", id);
            } else {
                log.error("User: {} haven't this product with id = {}", user.getEmail(), id);
            }
        } else {
            log.error("Product with id = {} is not found", id);
        }
    }

    @Transactional
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }


}
