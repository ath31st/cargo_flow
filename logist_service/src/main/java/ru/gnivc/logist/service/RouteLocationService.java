package ru.gnivc.logist.service;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gnivc.common.dto.RouteLocationDto;
import ru.gnivc.common.wrapper.RouteLocationWrapper;
import ru.gnivc.logist.entity.RouteLocation;
import ru.gnivc.logist.entity.TaskRoute;
import ru.gnivc.logist.repository.RouteLocationRepository;

@Service
@RequiredArgsConstructor
public class RouteLocationService {
  private final RouteLocationRepository routeLocationRepository;
  private final TaskRouteService taskRouteService;

  @Transactional
  public void createRouteLocation(RouteLocationWrapper wrapper) {
    RouteLocationDto dto = wrapper.routeLocationDto();
    TaskRoute tr = taskRouteService.getRoute(dto.companyId(), dto.taskId(), dto.taskRouteId());

    taskRouteService.checkExistingTaskRoute(
        dto.companyId(), dto.taskId(), wrapper.driverId(), dto.taskRouteId());
    taskRouteService.checkTaskRouteNotEndedOrNotCancelled(tr);

    saveRouteLocation(tr, dto);
  }

  private void saveRouteLocation(TaskRoute tr, RouteLocationDto dto) {
    RouteLocation rl = new RouteLocation();
    rl.setRoute(tr);
    rl.setLatitude(dto.latitude());
    rl.setLongitude(dto.longitude());
    rl.setRecordedAt(Instant.now());

    routeLocationRepository.save(rl);
  }
}
