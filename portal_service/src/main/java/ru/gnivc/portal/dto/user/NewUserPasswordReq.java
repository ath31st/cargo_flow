package ru.gnivc.portal.dto.user;

public record NewUserPasswordReq(
    String email,
    String newPassword
) {
}
