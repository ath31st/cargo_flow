package ru.gnivc.portal.dto.vehicle;

public record NewVehicleRegisterReq(
    String vin,
    String licensePlate,
    Integer year
) {
}
