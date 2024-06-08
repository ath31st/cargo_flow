package ru.gnivc.dwh.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.gnivc.common.exception.CompanyStatisticsServiceException;
import ru.gnivc.common.wrapper.StatisticsWrapper;
import ru.gnivc.dwh.service.CompanyStatisticsService;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsConsumer {
  private final ObjectMapper objectMapper;
  private final CompanyStatisticsService companyStatisticsService;

  @KafkaListener(topics = {"${kafka.topic.company_statistics.name}"},
      groupId = "${kafka.topic.company_statistics.group}")
  public void consumeMessage(String message) {
    log.info("message consumed {}", message);

    try {
      StatisticsWrapper wrapper = objectMapper.readValue(message, StatisticsWrapper.class);
      companyStatisticsService.saveOrUpdateStatistics(wrapper);
    } catch (JsonProcessingException | CompanyStatisticsServiceException e) {
      log.error(e.getMessage());
    }
  }
}
