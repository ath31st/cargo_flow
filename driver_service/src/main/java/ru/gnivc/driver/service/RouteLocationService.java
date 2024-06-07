package ru.gnivc.driver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.gnivc.common.client.LogistClient;
import ru.gnivc.common.dto.RouteLocationDto;
import ru.gnivc.common.dto.RouteLocationShortDto;
import ru.gnivc.common.exception.RouteEventServiceException;
import ru.gnivc.common.wrapper.RouteLocationWrapper;
import ru.gnivc.driver.dto.NewRouteLocationReq;
import ru.gnivc.driver.service.producer.RouteLocationProducer;

@Service
@RequiredArgsConstructor
public class RouteLocationService {
  private final LogistClient logistClient;
  private final RouteLocationProducer routeLocationProducer;

  public void createRouteLocation(Integer companyId, Integer taskId,
                                  Integer routeId, String driverId, NewRouteLocationReq req) {
    RouteLocationDto dto = new RouteLocationDto(companyId, taskId, routeId,
        req.latitude(), req.longitude());
    RouteLocationWrapper wrapper = new RouteLocationWrapper(driverId, dto);

    try {
      routeLocationProducer.sendMessage(wrapper);
    } catch (JsonProcessingException e) {
      throw new RouteEventServiceException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  public Page<RouteLocationShortDto> getPageRouteLocations(Integer companyId, Integer taskId,
                                                           Integer routeId, Pageable pageable) {
    return logistClient.getPageRouteLocations(companyId, taskId, routeId, pageable);
  }
}
