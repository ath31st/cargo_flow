package ru.gnivc.dwh.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gnivc.dwh.service.CompanyStatisticsService;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/dwh/v1/companies/{companyId}")
public class CompanyStatisticsController {
  private final CompanyStatisticsService companyStatisticsService;

  @GetMapping("/statistics")
  @PreAuthorize("@permissionValidator.hasCompanyAdminAccess(#companyId)")
  public ResponseEntity<String> getCompanyStatistics(@PathVariable Integer companyId) {
    return ResponseEntity.ok(companyStatisticsService.getStatistics(companyId));
  }
}
