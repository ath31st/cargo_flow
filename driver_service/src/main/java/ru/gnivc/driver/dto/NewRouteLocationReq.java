package ru.gnivc.driver.dto;

import java.math.BigDecimal;

public record NewRouteLocationReq(
    BigDecimal latitude,
    BigDecimal longitude
) {
}
