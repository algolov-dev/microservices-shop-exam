package com.techie.microservices.cat.repository;

import com.techie.microservices.cat.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
