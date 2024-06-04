package ru.gnivc.logist.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.gnivc.common.exception.TaskRouteServiceException;
import ru.gnivc.common.wrapper.RouteLocationWrapper;
import ru.gnivc.logist.service.RouteLocationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteLocationConsumer {
  private final RouteLocationService routeLocationService;
  private final ObjectMapper objectMapper;

  @KafkaListener(topics = {"${kafka.topic.route-locations.name}"},
      groupId = "${kafka.topic.route-locations.group}")
  public void consumeMessage(String message) {
    log.info("message consumed {}", message);

    try {
      RouteLocationWrapper wrapper = objectMapper.readValue(message, RouteLocationWrapper.class);
      routeLocationService.createRouteLocation(wrapper);
    } catch (JsonProcessingException | TaskRouteServiceException e) {
      log.error(e.getMessage());
    }
  }
}
