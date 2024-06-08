package ru.gnivc.dwh.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gnivc.dwh.service.CompanyStatisticsService;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/dwh/v1/companies/{companyId}")
public class CompanyStatisticsController {
  private final CompanyStatisticsService companyStatisticsService;

}
