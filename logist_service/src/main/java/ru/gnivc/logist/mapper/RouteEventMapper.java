package ru.gnivc.logist.mapper;

import org.springframework.stereotype.Component;
import ru.gnivc.common.dto.RouteEventShortDto;
import ru.gnivc.common.event.EventType;
import ru.gnivc.common.validator.EventTypeValidator;
import ru.gnivc.logist.entity.RouteEvent;

@Component
public class RouteEventMapper {

  public RouteEventShortDto toShortDto(RouteEvent event) {
    EventTypeValidator.validateIndex(event.getEventType());
    final String description = EventType.values()[event.getEventType()].getDescription();

    return new RouteEventShortDto(event.getId(), description, event.getEventTime());
  }
}
