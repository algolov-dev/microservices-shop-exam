package com.example.ReportService.service;

import com.example.ReportService.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final CatRepository catRepository;
    private final CityRepository cityRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public String generateSummaryReport() {
        long catCount = catRepository.count();
        long cityCount = cityRepository.count();
        long userCount = userRepository.count();
        long productCount = productRepository.count();

        return String.format(
                "Отчёт:\n" +
                        "- Всего котов: %d\n" +
                        "- Всего городов: %d\n" +
                        "- Всего пользователей: %d\n" +
                        "- Всего продуктов: %d\n",
                catCount, cityCount, userCount, productCount
        );
    }
}
