package ru.gnivc.dwh.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gnivc.dwh.repository.CompanyStatisticsRepository;

@Service
@RequiredArgsConstructor
public class CompanyStatisticsService {
  private final CompanyStatisticsRepository companyStatisticsRepository;

}
