package ru.gnivc.common.constants;

import lombok.Getter;

@Getter
public enum StringConstants {
  X_SERVICE_NAME("X-Service-name"),
  NOT_FOUND(" not found"),
  USER_WITH_EMAIL("User with email "),
  USER_WITH_ID("User with id ");

  private final String value;

  StringConstants(String value) {
    this.value = value;
  }
}
