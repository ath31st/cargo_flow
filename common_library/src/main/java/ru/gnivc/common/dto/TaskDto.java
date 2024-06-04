package ru.gnivc.common.dto;

import lombok.Builder;

@Builder
public record TaskDto(
    Integer id,
    String startPoint,
    String endPoint,
    String driverFullName,
    String cargoDescription,
    String companyVehicleLicensePlate
) {
}
