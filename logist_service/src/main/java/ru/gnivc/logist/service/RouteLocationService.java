package ru.gnivc.logist.service;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gnivc.logist.dto.RouteEventDto;
import ru.gnivc.logist.dto.RouteLocationDto;
import ru.gnivc.logist.entity.RouteLocation;
import ru.gnivc.logist.entity.TaskRoute;
import ru.gnivc.logist.repository.RouteLocationRepository;

@Service
@RequiredArgsConstructor
public class RouteLocationService {
  private final RouteLocationRepository routeLocationRepository;
  private final TaskRouteService taskRouteService;

  @Transactional
  public void saveRouteLocation(RouteLocationDto dto) {
    TaskRoute tr = taskRouteService.getRoute(dto.companyId(), dto.taskId(), dto.taskRouteId());

    taskRouteService.checkTaskRouteNotEndedOrNotCancelled(tr);

    RouteLocation rl = new RouteLocation();
    rl.setRoute(tr);
    rl.setLatitude(dto.latitude());
    rl.setLongitude(dto.longitude());
    rl.setRecordedAt(Instant.now());

    routeLocationRepository.save(rl);
  }
}
