package com.example.ReportService.controller;

import com.example.ReportService.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/summary")
    public String getSummary() {
        return reportService.generateSummaryReport();
    }

}
