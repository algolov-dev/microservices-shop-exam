package com.techie.microservices.cat.repository;

import com.techie.microservices.cat.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByTitle(String title);

    Page<Product> findByTitleContainingIgnoreCaseAndUser_ActiveTrue(String title, Pageable pageable);
    Page<Product> findByCityContainingIgnoreCaseAndUser_ActiveTrue(String city, Pageable pageable);
    Page<Product> findByTitleContainingIgnoreCaseAndCityContainingIgnoreCaseAndUser_ActiveTrue(String title, String city, Pageable pageable);
    Page<Product> findAllByUser_ActiveTrue(Pageable pageable);
}
