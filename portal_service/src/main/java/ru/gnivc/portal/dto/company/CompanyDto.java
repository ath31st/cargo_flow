package ru.gnivc.portal.dto.company;

public record CompanyDto(
    Integer id,
    String name,
    String address,
    String kpp,
    String ogrn,
    Integer quantityLogists,
    Integer quantityDrivers
) {
}
