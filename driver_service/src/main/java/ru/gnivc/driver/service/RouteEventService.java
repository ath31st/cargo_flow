package ru.gnivc.driver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.gnivc.common.client.LogistClient;
import ru.gnivc.common.dto.RouteEventDto;
import ru.gnivc.common.dto.RouteEventShortDto;
import ru.gnivc.common.exception.RouteEventServiceException;
import ru.gnivc.common.validator.EventTypeValidator;
import ru.gnivc.common.wrapper.RouteEventWrapper;
import ru.gnivc.driver.service.producer.RouteEventProducer;

@Service
@RequiredArgsConstructor
public class RouteEventService {
  private final LogistClient logistClient;
  private final RouteEventProducer routeEventProducer;

  public void createRouteEvent(Integer companyId, Integer taskId,
                               Integer routeId, Integer eventType, String driverId) {
    EventTypeValidator.validateIndex(eventType);

    RouteEventDto dto = new RouteEventDto(companyId, taskId, routeId, eventType);
    RouteEventWrapper wrapper = new RouteEventWrapper(driverId, dto);

    try {
      routeEventProducer.sendMessage(wrapper);
    } catch (JsonProcessingException e) {
      throw new RouteEventServiceException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  public Page<RouteEventShortDto> getPageRouteEvents(Integer companyId, Integer taskId,
                                                     Integer routeId, Pageable pageable) {
    return logistClient.getPageRouteEvents(companyId, taskId, routeId, pageable);
  }
}
