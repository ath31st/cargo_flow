package ru.gnivc.logist.service;

import static ru.gnivc.common.event.EventType.ROUTE_CANCELLED;
import static ru.gnivc.common.event.EventType.ROUTE_ENDED;

import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gnivc.common.dto.RouteEventDto;
import ru.gnivc.common.dto.RouteEventShortDto;
import ru.gnivc.common.event.EventType;
import ru.gnivc.common.validator.EventTypeValidator;
import ru.gnivc.common.wrapper.RouteEventWrapper;
import ru.gnivc.logist.entity.RouteEvent;
import ru.gnivc.logist.entity.TaskRoute;
import ru.gnivc.logist.mapper.RouteEventMapper;
import ru.gnivc.logist.repository.RouteEventRepository;

@Service
@RequiredArgsConstructor
public class RouteEventService {
  private final RouteEventRepository routeEventRepository;
  private final TaskRouteService taskRouteService;
  private final RouteEventMapper routeEventMapper;

  @Transactional
  public void createRouteEvent(RouteEventWrapper wrapper) {
    RouteEventDto dto = wrapper.routeEventDto();
    TaskRoute tr = taskRouteService.getRoute(dto.companyId(), dto.taskId(), dto.taskRouteId());

    taskRouteService.checkExistingTaskRoute(
        dto.companyId(), dto.taskId(), wrapper.driverId(), dto.taskRouteId());
    taskRouteService.checkTaskRouteNotEndedOrNotCancelled(tr);

    EventTypeValidator.validateIndex(dto.eventType());
    EventType eventType = EventType.values()[dto.eventType()];
    if (eventType == ROUTE_ENDED || eventType == ROUTE_CANCELLED) {
      tr.setEndTime(Instant.now());
    }

    saveRouteEvent(tr, dto);
  }

  private void saveRouteEvent(TaskRoute tr, RouteEventDto dto) {
    RouteEvent re = new RouteEvent();
    re.setRoute(tr);
    re.setEventType(dto.eventType());
    re.setEventTime(Instant.now());

    routeEventRepository.save(re);
  }

  public Page<RouteEventShortDto> getPageRouteEvents(Integer companyId, Integer taskId,
                                                     Integer routeId, Pageable pageable) {
    Page<RouteEvent> events =
        routeEventRepository.findPageRouteEvents(companyId, taskId, routeId, pageable);
    List<RouteEventShortDto> dtoList = events.stream()
        .map(routeEventMapper::toShortDto)
        .toList();

    return new PageImpl<>(dtoList, pageable, events.getTotalElements());
  }
}
