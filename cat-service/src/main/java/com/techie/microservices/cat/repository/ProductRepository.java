package com.techie.microservices.cat.repository;

import com.techie.microservices.cat.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByTitle(String title);

    Page<Product> findByTitleContainingIgnoreCaseAndUser_ActiveTrue(String title, Pageable pageable);
    Page<Product> findByCategoryContainingIgnoreCaseAndUser_ActiveTrue(String category, Pageable pageable);
    Page<Product> findByTitleContainingIgnoreCaseAndCategoryContainingIgnoreCaseAndUser_ActiveTrue(String title, String category, Pageable pageable);
    Page<Product> findAllByUser_ActiveTrue(Pageable pageable);
    Page<Product> findByTags_NameIgnoreCaseAndUser_ActiveTrue(String tagName, Pageable pageable);
}
