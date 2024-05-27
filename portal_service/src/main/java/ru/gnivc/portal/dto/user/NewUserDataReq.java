package ru.gnivc.portal.dto.user;

public record NewUserDataReq(
    String newFirstName,
    String newLastName,
    String newEmail
) {
}
