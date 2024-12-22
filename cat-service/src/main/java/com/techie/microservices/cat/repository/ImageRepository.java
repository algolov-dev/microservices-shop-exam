package com.techie.microservices.cat.repository;

import com.techie.microservices.cat.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
