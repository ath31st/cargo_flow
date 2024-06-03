package ru.gnivc.logist.service;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gnivc.common.validator.EventTypeValidator;
import ru.gnivc.logist.dto.NewRouteEventReq;
import ru.gnivc.logist.entity.RouteEvent;
import ru.gnivc.logist.entity.TaskRoute;
import ru.gnivc.logist.repository.RouteEventRepository;

@Service
@RequiredArgsConstructor
public class RouteEventService {
  private final RouteEventRepository routeEventRepository;
  private final TaskRouteService taskRouteService;

  @Transactional
  public void createRouteEvent(NewRouteEventReq req) {
    TaskRoute tr = taskRouteService.getRoute(req.companyId(), req.taskId(), req.taskRouteId());
    EventTypeValidator.validateIndex(req.eventType());

    RouteEvent re = new RouteEvent();
    re.setRoute(tr);
    re.setEventType(req.eventType());
    re.setEventTime(Instant.now());

    routeEventRepository.save(re);
  }
}
