package ru.gnivc.logist.mapper;

import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gnivc.common.event.EventType;
import ru.gnivc.common.validator.EventTypeValidator;
import ru.gnivc.common.dto.NewTaskRouteReq;
import ru.gnivc.common.dto.TaskRouteDto;
import ru.gnivc.logist.entity.RouteEvent;
import ru.gnivc.logist.entity.Task;
import ru.gnivc.logist.entity.TaskRoute;

@Component
@RequiredArgsConstructor
public class TaskRouteMapper {

  public TaskRoute toEntity(Task task, NewTaskRouteReq req) {
    TaskRoute tr = new TaskRoute();
    tr.setTask(task);
    tr.setCreatedAt(Instant.now());
    tr.setStartTime(req.startTime());
    tr.setRouteLocations(Collections.emptySet());
    tr.setRouteEvents(Collections.emptySet());

    return tr;
  }

  public TaskRouteDto toDto(TaskRoute route) {
    Optional<RouteEvent> latestEvent = route.getRouteEvents().stream()
        .max(Comparator.comparing(RouteEvent::getEventTime));

    String eventDescription = latestEvent.map(event -> {
      int eventTypeIndex = event.getEventType();

      EventTypeValidator.validateIndex(eventTypeIndex);

      return EventType.values()[eventTypeIndex].getDescription();
    }).orElse("No event found");

    return TaskRouteDto.builder()
        .id(route.getId())
        .createdAt(route.getCreatedAt())
        .startTime(route.getStartTime())
        .endTime(route.getEndTime())
        .currentRouteEvent(eventDescription)
        .build();
  }
}
