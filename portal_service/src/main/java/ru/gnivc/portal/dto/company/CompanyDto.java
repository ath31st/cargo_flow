package ru.gnivc.portal.dto.company;

public record CompanyDto(
    Integer id,
    String name,
    String inn,
    String address,
    String kpp,
    String ogrn,
    Long quantityLogists,
    Long quantityDrivers
) {
}
