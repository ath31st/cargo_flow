package ru.gnivc.logist.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.gnivc.logist.dto.RouteEventDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteEventConsumer {
  private final RouteEventService routeEventService;
  private final ObjectMapper objectMapper;

  @KafkaListener(topics = {"${kafka.topic.name}"}, groupId = "${kafka.topic.group}")
  public void consumeMessage(String message) {
    log.info("message consumed {}", message);

    RouteEventDto dto = objectMapper.convertValue(message, RouteEventDto.class);
    routeEventService.saveRouteEvent(dto);
  }
}
