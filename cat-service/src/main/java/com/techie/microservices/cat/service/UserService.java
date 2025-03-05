package com.techie.microservices.cat.service;

import com.techie.microservices.cat.model.User;
import com.techie.microservices.cat.model.enums.Role;
import com.techie.microservices.cat.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean createUser(User user) {

        String email = user.getEmail();
        if (userRepository.findByEmail(email) != null) return false;
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        log.info("Saving new User with email: {}", email);
        userRepository.save(user);
        return true;
    }

    public List<User> list() {
        return userRepository.findAll();
    }
    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();   // Для Freemarker
        return userRepository.findByEmail(principal.getName());
    }



    public void banUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        log.info("user {}", user);
        if (user != null) {
            log.info("user with {}", user.isActive());
            if (user.isActive()) {
                user.setActive(false);
                log.info("Ban user with id = {}; email: {}", user.getId(), user.getEmail());
            } else {
                user.setActive(true);
                log.info("Unban user with id = {}; email: {}", user.getId(), user.getEmail());
            }
        }
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(User user, String phoneNumber, String name, Map<String, String> form) {

        user.setPhone_number(phoneNumber);
        user.setName(name);

        user.getRoles().clear();
        if (form.containsKey("role")) {
            user.getRoles().add(Role.valueOf(form.get("role")));
        }

        userRepository.save(user);

    }
}
