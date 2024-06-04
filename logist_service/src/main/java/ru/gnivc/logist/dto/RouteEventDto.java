package ru.gnivc.logist.dto;

public record RouteEventDto(
    Integer companyId,
    Integer taskId,
    Integer taskRouteId,
    Integer eventType
) {
}
