package ru.gnivc.logist.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
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

  @Scheduled(fixedRate = 10000, initialDelay = 5000)
  private void test() {
    log.info("test scheduler");
  }
}
