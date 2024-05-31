package ru.gnivc.logist.dto;

public record NewTaskReq(
    String startPoint,
    String endPoint,
    String driverKeycloakId,
    Integer companyVehicleId,
    String cargoDescription
) {
}
