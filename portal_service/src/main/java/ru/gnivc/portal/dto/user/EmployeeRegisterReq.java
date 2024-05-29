package ru.gnivc.portal.dto.user;

public record EmployeeRegisterReq(
    String firstName,
    String lastName,
    String email,
    String role
) {
}
