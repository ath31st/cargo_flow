package ru.gnivc.logist.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.gnivc.common.exception.TaskRouteServiceException;
import ru.gnivc.common.wrapper.RouteEventWrapper;
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
      RouteEventWrapper wrapper = objectMapper.readValue(message, RouteEventWrapper.class);
      routeEventService.saveRouteEvent(wrapper);
    } catch (JsonProcessingException | TaskRouteServiceException e) {
      log.error(e.getMessage());
    }
  }
}
