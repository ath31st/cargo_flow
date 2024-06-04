package ru.gnivc.common.wrapper;

import ru.gnivc.common.dto.RouteLocationDto;

public record RouteLocationWrapper(
    String driverId,
    RouteLocationDto routeLocationDto
) {
}
