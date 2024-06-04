package ru.gnivc.logist.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.gnivc.common.dto.RouteEventDto;
import ru.gnivc.common.exception.RouteEventServiceException;
import ru.gnivc.logist.service.RouteEventService;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteEventConsumer {
  private final RouteEventService routeEventService;
  private final ObjectMapper objectMapper;

  @KafkaListener(topics = {"${kafka.topic.route-events.name}"},
      groupId = "${kafka.topic.route-events.group}")
  public void consumeMessage(String message) {
    log.info("message consumed {}", message);

    try {
      RouteEventDto dto = objectMapper.readValue(message, RouteEventDto.class);
      routeEventService.saveRouteEvent(dto);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
      throw new RouteEventServiceException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }
}
