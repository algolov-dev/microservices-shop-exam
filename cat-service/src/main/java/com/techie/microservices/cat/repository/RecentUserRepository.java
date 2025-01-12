package com.techie.microservices.cat.repository;

import com.techie.microservices.cat.model.RecentUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecentUserRepository extends JpaRepository<RecentUser, Long> {
    // Дополнительные методы поиска при необходимости
}
