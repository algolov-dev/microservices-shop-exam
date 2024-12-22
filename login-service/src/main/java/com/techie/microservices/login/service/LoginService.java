package com.techie.microservices.login.service;

import com.techie.microservices.login.model.User;
import com.techie.microservices.login.repository.LoginRepository;
import com.techie.microservices.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final LoginRepository loginRepository;

    private final UserRepository userRepository;
    public boolean isInStock(String email, String password) {
        List<User> allUsers = userRepository.findAll();
        log.info("All users: {}", allUsers);
        log.info(" Start -- Received request to check stock for skuCode {}, with quantity {}", email, password);
        boolean isInStock = loginRepository.existsByEmailAndPassword(email, password);
        log.info(" End -- Product with skuCode {}, and quantity {}, is in stock - {}", email, password, isInStock);
        return isInStock;
    }
}
