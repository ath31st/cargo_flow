package ru.gnivc.driver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.gnivc.common.dto.RouteEventDto;
import ru.gnivc.common.exception.RouteEventServiceException;
import ru.gnivc.common.validator.EventTypeValidator;

@Service
@RequiredArgsConstructor
public class RouteEventService {
  private final RouteEventProducer routeEventProducer;

  public void createRouteEvent(Integer companyId, Integer taskId,
                               Integer routeId, Integer eventType) {
    EventTypeValidator.validateIndex(eventType);
    RouteEventDto dto = new RouteEventDto(companyId, taskId, routeId, eventType);
    try {
      routeEventProducer.sendMessage(dto);
    } catch (JsonProcessingException e) {
      throw new RouteEventServiceException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }
}
