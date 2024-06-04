package ru.gnivc.driver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.gnivc.common.dto.RouteEventDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteEventProducer {
  @Value("${kafka.topic.route-events.name}")
  private String routeEventsTopic;

  private final ObjectMapper objectMapper;
  private final KafkaTemplate<String, String> kafkaTemplate;

  public void sendMessage(RouteEventDto dto) throws JsonProcessingException {
    String routeEventAsMessage = objectMapper.writeValueAsString(dto);

    CompletableFuture<SendResult<String, String>> future
        = kafkaTemplate.send(routeEventsTopic, routeEventAsMessage);

    future.whenComplete((result, ex) -> {
      if (ex == null) {
        log.info("Sent message=[{}] with offset=[{}]", routeEventAsMessage,
            result.getRecordMetadata().offset());
      } else {
        log.error("Unable to send message=[{}] due to : {}", routeEventAsMessage, ex.getMessage());
      }
    });
  }
}
