package ru.gnivc.logist.dto;

public record NewRouteEventReq(
    Integer companyId,
    Integer taskId,
    Integer taskRouteId,
    Integer eventType
) {
}
