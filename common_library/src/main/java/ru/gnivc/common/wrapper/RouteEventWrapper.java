package ru.gnivc.common.wrapper;

import ru.gnivc.common.dto.RouteEventDto;

public record RouteEventWrapper(
    String driverId,
    RouteEventDto routeEventDto
) {
}
