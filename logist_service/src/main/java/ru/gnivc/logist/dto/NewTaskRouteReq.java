package ru.gnivc.logist.dto;

import java.time.Instant;

public record NewTaskRouteReq(
    Instant startTime
) {
}
