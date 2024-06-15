package ru.gnivc.common.constants;

import lombok.Getter;

@Getter
public enum StringConstants {
  X_SERVICE_NAME("X-Service-name");

  private final String value;

  StringConstants(String value) {
    this.value = value;
  }
}
