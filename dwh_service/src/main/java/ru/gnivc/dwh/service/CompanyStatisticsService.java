package ru.gnivc.dwh.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.gnivc.common.exception.CompanyServiceException;
import ru.gnivc.common.wrapper.StatisticsWrapper;
import ru.gnivc.dwh.entity.CompanyStatistics;
import ru.gnivc.dwh.repository.CompanyStatisticsRepository;

@Service
@RequiredArgsConstructor
public class CompanyStatisticsService {
  private final CompanyStatisticsRepository companyStatisticsRepository;

  public void saveOrUpdateStatistics(StatisticsWrapper wrapper) {
    Optional<CompanyStatistics> existingStatistics =
        companyStatisticsRepository.findById(wrapper.companyId());

    CompanyStatistics cs;
    if (existingStatistics.isPresent()) {
      cs = existingStatistics.get();
    } else {
      cs = new CompanyStatistics();
      cs.setCompanyId(wrapper.companyId());
    }

    cs.setRawStatistics(wrapper.rawStatistics());
    companyStatisticsRepository.save(cs);
  }

  public String getStatistics(Integer companyId) {
    CompanyStatistics cs = getCompanyStatistics(companyId);
    return cs.getRawStatistics();
  }

  private CompanyStatistics getCompanyStatistics(Integer companyId) {
    return companyStatisticsRepository.findById(companyId)
        .orElseThrow(() -> new CompanyServiceException(HttpStatus.NOT_FOUND,
            "Statistics by company with id: " + companyId + " not found"));
  }
}
