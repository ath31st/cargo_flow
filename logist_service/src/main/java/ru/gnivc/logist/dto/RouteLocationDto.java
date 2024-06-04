package ru.gnivc.logist.dto;

import java.math.BigDecimal;

public record RouteLocationDto(
    Integer companyId,
    Integer taskId,
    Integer taskRouteId,
    BigDecimal latitude,
    BigDecimal longitude
) {
}
