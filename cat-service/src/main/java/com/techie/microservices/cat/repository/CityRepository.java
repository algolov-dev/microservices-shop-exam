package com.techie.microservices.cat.repository;

import com.techie.microservices.cat.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
}
