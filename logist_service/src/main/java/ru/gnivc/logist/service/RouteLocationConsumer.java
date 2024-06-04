package ru.gnivc.logist.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.gnivc.common.dto.RouteLocationDto;

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

    RouteLocationDto dto = objectMapper.convertValue(message, RouteLocationDto.class);
    routeLocationService.saveRouteLocation(dto);
  }
}
