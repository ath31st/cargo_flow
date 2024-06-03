package ru.gnivc.logist.service;

import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.gnivc.common.event.EventType;
import ru.gnivc.common.exception.TaskRouteServiceException;
import ru.gnivc.logist.dto.NewTaskRouteReq;
import ru.gnivc.logist.dto.TaskRouteDto;
import ru.gnivc.logist.entity.RouteEvent;
import ru.gnivc.logist.entity.TaskRoute;
import ru.gnivc.logist.repository.TaskRouteRepository;

@Service
@RequiredArgsConstructor
public class TaskRouteService {
  private final TaskRouteRepository taskRouteRepository;
  private final TaskService taskService;

  public TaskRouteDto getRouteDto(Integer companyId, Integer taskId, Integer routeId) {
    return null;
  }

  private TaskRoute getRoute(Integer companyId, Integer taskId, Integer routeId) {
    final Optional<TaskRoute> optionalRoute =
        taskRouteRepository.findTaskRoute(companyId, taskId, routeId);
    return optionalRoute.orElseThrow(() -> new TaskRouteServiceException(HttpStatus.NOT_FOUND,
        "Task route with id: " + routeId + " not found"));
  }

  @Transactional
  public void createTaskRoute(Integer companyId, Integer taskId, NewTaskRouteReq req) {
    TaskRoute tr = new TaskRoute();
    tr.setTask(taskService.getTask(companyId, taskId));
    tr.setCreatedAt(Instant.now());
    tr.setStartTime(req.startTime());
    tr.setRouteLocations(Collections.emptySet());

    RouteEvent re = new RouteEvent();
    re.setEventTime(Instant.now());
    re.setEventType(EventType.ROUTE_CREATED.ordinal());
    re.setRoute(tr);
    tr.setRouteEvents(Set.of(re));

    taskRouteRepository.save(tr);
  }
}
