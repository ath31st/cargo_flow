package ru.gnivc.driver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
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
    kafkaTemplate.send(routeEventsTopic, routeEventAsMessage);

    log.info("route event produced {}", routeEventAsMessage);
  }
}
