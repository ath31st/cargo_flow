package ru.gnivc.common.dto;

import java.time.Instant;

public record RouteEventShortDto(
    Integer eventId,
    String eventType,
    Instant eventTime
) {
}
