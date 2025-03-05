package com.techie.microservices.cat.service;

import com.techie.microservices.cat.model.*;
import com.techie.microservices.cat.repository.CategoryRepository;
import com.techie.microservices.cat.repository.ProductRepository;
import com.techie.microservices.cat.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final TagService tagService;

    @Transactional
    public Page<Product> listProductsByTag(String tag, Pageable pageable) {
        return productRepository.findByTags_NameIgnoreCaseAndUser_ActiveTrue(tag, pageable);
    }

    @Transactional
    public Page<Product> listProducts(String title, String category, Pageable pageable) {
        boolean hasTitle = title != null && !title.trim().isEmpty();
        boolean hasCategory = category != null && !category.trim().isEmpty();

        if (!hasTitle && !hasCategory) {
            return productRepository.findAllByUser_ActiveTrue(pageable);
        } else if (hasTitle && !hasCategory) {
            return productRepository.findByTitleContainingIgnoreCaseAndUser_ActiveTrue(title, pageable);
        } else if (!hasTitle && hasCategory) {
            return productRepository.findByCategoryContainingIgnoreCaseAndUser_ActiveTrue(category, pageable);
        } else {
            return productRepository.findByTitleContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndUser_ActiveTrue(title, category, pageable);
        }
    }



    @Transactional
    public void saveProduct(Principal principal, Product product, List<MultipartFile> files, List<String> tagNames) throws IOException {
        log.info("Начало сохранения продукта: {}", product.getTitle());
        User user = getUserByPrincipal(principal);
        product.setUser(user);
        log.info("Установлен пользователь: {}", user.getEmail());

        if (files != null && !files.isEmpty()) {
            log.info("Получено {} файлов для загрузки.", files.size());
            if (files.size() > 10) {
                log.error("Попытка загрузить более 10 фотографий для продукта: {}", product.getTitle());
                throw new IllegalArgumentException("Нельзя загрузить более 10 фотографий.");
            }
            boolean isFirst = true;
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    log.debug("Пропущен пустой файл.");
                    continue;
                }
                log.info("Обработка файла: {}", file.getOriginalFilename());
                Image image = toImageEntity(file);
                if (isFirst) {
                    image.setPreviewImage(true);
                    isFirst = false;
                    log.info("Установлено превью изображение для продукта.");
                }
                product.addImageToProduct(image);
            }
            if (product.getImages().isEmpty()) {
                log.error("После обработки файлов не осталось изображений для продукта: {}", product.getTitle());
                throw new IllegalArgumentException("Пожалуйста, загрузите хотя бы одну фотографию.");
            }
        } else {
            log.error("Файлы не переданы для продукта: {}", product.getTitle());
            throw new IllegalArgumentException("Пожалуйста, загрузите хотя бы одну фотографию.");
        }

        if (tagNames != null && !tagNames.isEmpty()) {
            log.info("Получено {} тегов для обработки.", tagNames.size());
            for (String tagName : tagNames) {
                tagName = tagName.trim();
                if (!tagName.isEmpty()) {
                    log.info("Обработка тега: {}", tagName);
                    Tag tag = tagService.findByName(tagName);
                    if (tag == null) {
                        log.info("Тег '{}' не найден в базе, создаём новый.", tagName);
                        tag = new Tag();
                        tag.setName(tagName);
                        tag = tagService.save(tag);
                        log.info("Новый тег '{}' сохранён с ID: {}", tagName, tag.getId());
                    }
                    product.addTag(tag);
                    log.info("Тег '{}' добавлен к продукту.", tagName);
                }
            }
        } else {
            log.info("Теги не переданы для продукта: {}", product.getTitle());
        }

        Product savedProduct = productRepository.save(product);
        log.info("Продукт '{}' сохранён с ID: {}", product.getTitle(), savedProduct.getId());

        if (!savedProduct.getImages().isEmpty()) {
            savedProduct.setPreviewImageId(savedProduct.getImages().get(0).getId());
            productRepository.save(savedProduct);
        } else {
            log.warn("Нет загруженных изображений для продукта: {}", product.getTitle());
        }
        log.info("Завершено сохранение продукта: {}", savedProduct.getTitle());
    }

    @Transactional
    public void updateProduct(Principal principal, Product updatedProduct, List<MultipartFile> files, List<String> tagNames) throws IOException {
        log.info("Начало обновления продукта с ID: {}", updatedProduct.getId());

        Product existingProduct = productRepository.findById(updatedProduct.getId())
                .orElseThrow(() -> new IllegalArgumentException("Продукт не найден с ID: " + updatedProduct.getId()));

        User currentUser = getUserByPrincipal(principal);
        if (!existingProduct.getUser().getId().equals(currentUser.getId())) {
            log.error("Пользователь {} не является владельцем продукта с ID: {}", currentUser.getEmail(), updatedProduct.getId());
            throw new IllegalArgumentException("Нет прав для обновления продукта");
        }

        // Обновляем основные поля
        existingProduct.setTitle(updatedProduct.getTitle());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setCategory(updatedProduct.getCategory());
        log.info("Обновлены основные поля продукта с ID: {}", existingProduct.getId());

        // Обработка новых изображений (добавление к уже существующим)
        if (files != null && !files.isEmpty()) {
            log.info("Получено {} новых файлов для обновления продукта с ID: {}", files.size(), existingProduct.getId());
            if (files.size() > 10) {
                log.error("Попытка загрузить более 10 файлов для продукта с ID: {}", existingProduct.getId());
                throw new IllegalArgumentException("Нельзя загрузить более 10 фотографий.");
            }
            boolean isPreview = existingProduct.getImages().isEmpty();
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    log.debug("Пропущен пустой файл.");
                    continue;
                }
                log.info("Обработка файла: {}", file.getOriginalFilename());
                Image image = toImageEntity(file);
                if (isPreview) {
                    image.setPreviewImage(true);
                    isPreview = false;
                    log.info("Установлено превью изображение для продукта с ID: {}", existingProduct.getId());
                }
                existingProduct.addImageToProduct(image);
            }
            if (existingProduct.getImages().isEmpty()) {
                log.error("После обработки файлов не осталось изображений для продукта с ID: {}", existingProduct.getId());
                throw new IllegalArgumentException("Пожалуйста, загрузите хотя бы одну фотографию.");
            }
            // Обновляем previewImageId, если изображения существуют
            existingProduct.setPreviewImageId(existingProduct.getImages().get(0).getId());
        } else {
            log.info("Новые изображения не переданы для продукта с ID: {}", existingProduct.getId());
        }

        existingProduct.getTags().clear();
        if (tagNames != null && !tagNames.isEmpty()) {
            log.info("Получено {} тегов для обновления продукта с ID: {}", tagNames.size(), existingProduct.getId());
            for (String tagName : tagNames) {
                tagName = tagName.trim();
                if (!tagName.isEmpty()) {
                    log.info("Обработка тега: {}", tagName);
                    Tag tag = tagService.findByName(tagName);
                    if (tag == null) {
                        log.info("Тег '{}' не найден, создаём новый.", tagName);
                        tag = new Tag();
                        tag.setName(tagName);
                        tag = tagService.save(tag);
                        log.info("Новый тег '{}' сохранён с ID: {}", tagName, tag.getId());
                    }
                    existingProduct.addTag(tag);
                    log.info("Тег '{}' добавлен к продукту с ID: {}", tagName, existingProduct.getId());
                }
            }
        } else {
            log.info("Теги не переданы для обновления продукта с ID: {}", existingProduct.getId());
        }

        // Сохраняем обновленный продукт
        productRepository.save(existingProduct);
        log.info("Обновление продукта с ID: {} завершено", existingProduct.getId());
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

    @Transactional
    public void deleteImage(Product product, Long imageId) {
        // Находим изображение по ID среди изображений продукта
        Image imageToDelete = product.getImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElse(null);

        if (imageToDelete != null) {
            product.getImages().remove(imageToDelete);
            // Если удаляется превью-изображение, переназначаем его
            if (imageToDelete.isPreviewImage() && !product.getImages().isEmpty()) {
                product.setPreviewImageId(product.getImages().get(0).getId());
                product.getImages().get(0).setPreviewImage(true);
            }
            productRepository.save(product);
            log.info("Изображение с ID {} удалено из продукта '{}'", imageId, product.getTitle());
        } else {
            log.warn("Изображение с ID {} не найдено в продукте '{}'", imageId, product.getTitle());
        }
    }

}
