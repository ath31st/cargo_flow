package ru.gnivc.common.dto;

public record RouteEventDto(
    Integer companyId,
    Integer taskId,
    Integer taskRouteId,
    Integer eventType
) {
}
