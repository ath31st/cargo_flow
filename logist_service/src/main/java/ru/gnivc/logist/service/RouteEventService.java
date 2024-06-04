package ru.gnivc.logist.service;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gnivc.common.validator.EventTypeValidator;
import ru.gnivc.common.dto.RouteEventDto;
import ru.gnivc.logist.entity.RouteEvent;
import ru.gnivc.logist.entity.TaskRoute;
import ru.gnivc.logist.repository.RouteEventRepository;

@Service
@RequiredArgsConstructor
public class RouteEventService {
  private final RouteEventRepository routeEventRepository;
  private final TaskRouteService taskRouteService;

  @Transactional
  public void saveRouteEvent(RouteEventDto dto) {
    TaskRoute tr = taskRouteService.getRoute(dto.companyId(), dto.taskId(), dto.taskRouteId());

    taskRouteService.checkTaskRouteNotEndedOrNotCancelled(tr);
    EventTypeValidator.validateIndex(dto.eventType());

    RouteEvent re = new RouteEvent();
    re.setRoute(tr);
    re.setEventType(dto.eventType());
    re.setEventTime(Instant.now());

    routeEventRepository.save(re);
  }
}
