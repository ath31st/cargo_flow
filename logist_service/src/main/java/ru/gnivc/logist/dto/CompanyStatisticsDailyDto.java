package ru.gnivc.logist.dto;

import lombok.Builder;

@Builder
public record CompanyStatisticsDailyDto(
    Integer companyId,
    Integer countTasks,
    Integer countStartedRoutes,
    Integer countEndedRoutes,
    Integer countCanceledRoutes
) {
}
