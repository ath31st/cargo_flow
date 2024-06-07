package ru.gnivc.common.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record RouteLocationShortDto(
    Integer locationId,
    BigDecimal latitude,
    BigDecimal longitude,
    Instant recordedAt
) {
}
