package ru.gnivc.logist.service;

import static ru.gnivc.common.event.EventType.ROUTE_CANCELLED;
import static ru.gnivc.common.event.EventType.ROUTE_ENDED;
import static ru.gnivc.common.event.EventType.ROUTE_STARTED;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gnivc.logist.dto.CompanyStatisticsDailyDto;
import ru.gnivc.logist.repository.TaskRepository;
import ru.gnivc.logist.repository.TaskRouteRepository;

@Service
@RequiredArgsConstructor
public class StatisticsService {
  private final TaskRepository taskRepository;
  private final TaskRouteRepository taskRouteRepository;

  @Transactional
  public CompanyStatisticsDailyDto getDailyStatistics(Integer companyId) {
    int createdTasksAtToday = taskRepository.countByCompanyIdAndCreatedAtToday(companyId);
    int countStartedRoutes =
        taskRouteRepository.countByCompanyIdAndLastEventType(companyId, ROUTE_STARTED.ordinal());
    int countEndedRoutes =
        taskRouteRepository.countByCompanyIdAndLastEventType(companyId, ROUTE_ENDED.ordinal());
    int countCanceledRoutes =
        taskRouteRepository.countByCompanyIdAndLastEventType(companyId, ROUTE_CANCELLED.ordinal());

    return CompanyStatisticsDailyDto.builder()
        .companyId(companyId)
        .countTasks(createdTasksAtToday)
        .countStartedRoutes(countStartedRoutes)
        .countEndedRoutes(countEndedRoutes)
        .countCanceledRoutes(countCanceledRoutes)
        .build();
  }
}
