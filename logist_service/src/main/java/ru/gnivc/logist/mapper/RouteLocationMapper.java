package ru.gnivc.logist.mapper;

import org.springframework.stereotype.Component;
import ru.gnivc.common.dto.RouteLocationShortDto;
import ru.gnivc.logist.entity.RouteLocation;

@Component
public class RouteLocationMapper {

  public RouteLocationShortDto toShortDto(RouteLocation location) {

    return new RouteLocationShortDto(location.getId(), location.getLatitude(),
        location.getLongitude(), location.getRecordedAt());
  }
}
