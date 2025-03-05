package com.techie.microservices.cat.repository;

import com.techie.microservices.cat.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("SELECT t FROM Tag t LEFT JOIN t.products p GROUP BY t.id ORDER BY COUNT(p) DESC")
    List<Tag> findTopTags();

    Optional<Tag> findByName(String name);
}
