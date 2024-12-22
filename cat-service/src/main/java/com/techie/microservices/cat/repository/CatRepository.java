package com.techie.microservices.cat.repository;

import com.techie.microservices.cat.model.Cat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatRepository extends JpaRepository<Cat, Long> {
}
