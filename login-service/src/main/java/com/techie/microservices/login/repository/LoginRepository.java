package com.techie.microservices.login.repository;

import com.techie.microservices.login.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Login, Long> {
    boolean existsByEmailAndPassword(String Email, String Password);
}
