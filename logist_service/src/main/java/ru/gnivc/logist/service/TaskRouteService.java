package ru.gnivc.logist.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.gnivc.common.exception.TaskRouteServiceException;
import ru.gnivc.logist.dto.TaskRouteDto;
import ru.gnivc.logist.entity.TaskRoute;
import ru.gnivc.logist.repository.TaskRouteRepository;

@Service
@RequiredArgsConstructor
public class TaskRouteService {
  private final TaskRouteRepository taskRouteRepository;

  public TaskRouteDto getRouteDto(Integer companyId, Integer taskId, Integer routeId) {

  }

  private TaskRoute getRoute(Integer companyId, Integer taskId, Integer routeId) {
    final Optional<TaskRoute> optionalRoute =
        taskRouteRepository.findTaskRoute(companyId, taskId, routeId);
    return optionalRoute.orElseThrow(() -> new TaskRouteServiceException(HttpStatus.NOT_FOUND,
        "Task route with id: " + routeId + " not found"));
  }
}
