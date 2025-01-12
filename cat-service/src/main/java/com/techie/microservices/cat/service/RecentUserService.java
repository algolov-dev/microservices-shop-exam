package com.techie.microservices.cat.service;

import com.techie.microservices.cat.model.RecentUser;
import com.techie.microservices.cat.repository.RecentUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecentUserService {

    private final RecentUserRepository recentUserRepository;

    public RecentUserService(RecentUserRepository recentUserRepository) {
        this.recentUserRepository = recentUserRepository;
    }

    public List<RecentUser> getRecentUsers() {
        return recentUserRepository.findAll();
    }
}
