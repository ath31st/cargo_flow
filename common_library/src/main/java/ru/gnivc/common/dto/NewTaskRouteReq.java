package ru.gnivc.common.dto;

import java.time.Instant;

public record NewTaskRouteReq(
    Instant startTime
) {
}
