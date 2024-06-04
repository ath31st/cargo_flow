package ru.gnivc.logist.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.gnivc.common.dto.RouteLocationDto;
import ru.gnivc.common.exception.RouteEventServiceException;
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
      RouteLocationDto dto = objectMapper.readValue(message, RouteLocationDto.class);
      routeLocationService.saveRouteLocation(dto);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
      throw new RouteEventServiceException(HttpStatus.BAD_REQUEST, e.getMessage());
    }

  }
}
