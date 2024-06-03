package ru.gnivc.logist.dto;

import java.time.Instant;

public record TaskRouteDto(
    Integer id,
    Instant createdAt,
    Instant startTime,
    Instant endTime,
    String currentRouteEvent
) {
}
