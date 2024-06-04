package ru.gnivc.driver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.gnivc.driver.dto.NewRouteLocationReq;
import ru.gnivc.common.dto.RouteLocationDto;
import ru.gnivc.common.exception.RouteEventServiceException;
import ru.gnivc.common.wrapper.RouteLocationWrapper;

@Service
@RequiredArgsConstructor
public class RouteLocationService {
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
}
