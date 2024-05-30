package ru.gnivc.portal.dto.company;

import lombok.Builder;

@Builder
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
