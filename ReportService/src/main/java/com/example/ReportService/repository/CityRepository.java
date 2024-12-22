package com.example.ReportService.repository;

import com.example.ReportService.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
}
