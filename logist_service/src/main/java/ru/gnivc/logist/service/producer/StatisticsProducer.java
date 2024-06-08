package ru.gnivc.logist.service.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.gnivc.common.wrapper.StatisticsWrapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsProducer {
  @Value("${kafka.topic.company_statistics.name}")
  private String companyStatisticsTopic;

  private final ObjectMapper objectMapper;
  private final KafkaTemplate<String, String> kafkaTemplate;

  public void sendMessage(StatisticsWrapper wrapper) throws JsonProcessingException {
    String statisticsAsMessage = objectMapper.writeValueAsString(wrapper);

    CompletableFuture<SendResult<String, String>> future
        = kafkaTemplate.send(companyStatisticsTopic, statisticsAsMessage);

    future.whenComplete((result, ex) -> {
      if (ex == null) {
        log.info("Sent message=[{}] with offset=[{}]", statisticsAsMessage,
            result.getRecordMetadata().offset());
      } else {
        log.error("Unable to send message=[{}] due to : {}", statisticsAsMessage, ex.getMessage());
      }
    });
  }
}
