package ru.gnivc.logist.dto;

import java.time.Instant;
import lombok.Builder;

@Builder
public record TaskRouteDto(
    Integer id,
    Instant createdAt,
    Instant startTime,
    Instant endTime,
    String currentRouteEvent
) {
}
