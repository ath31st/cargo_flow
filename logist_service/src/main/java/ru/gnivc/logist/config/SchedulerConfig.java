package ru.gnivc.logist.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.gnivc.common.client.PortalClient;
import ru.gnivc.common.wrapper.StatisticsWrapper;
import ru.gnivc.logist.dto.CompanyStatisticsDailyDto;
import ru.gnivc.logist.service.StatisticsService;
import ru.gnivc.logist.service.producer.StatisticsProducer;

@Slf4j
@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
@RequiredArgsConstructor
public class SchedulerConfig {
  private final StatisticsProducer statisticsProducer;
  private final StatisticsService statisticsService;
  private final PortalClient portalClient;
  private final ObjectMapper objectMapper;

  @Scheduled(fixedRate = 10000, initialDelay = 5000)
  private void test() {
    try {
      List<Integer> companyIds = portalClient.getCompanyIds();
      if (!companyIds.isEmpty()) {
        for (Integer companyId : companyIds) {
          CompanyStatisticsDailyDto dto = statisticsService.getDailyStatistics(companyId);
          statisticsProducer.sendMessage(
              new StatisticsWrapper(companyId, objectMapper.writeValueAsString(dto)));
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }
}
