package ru.gnivc.portal.dto.user;

public record IndividualRegisterReq(
    String firstName,
    String lastName,
    String email
) {
}
