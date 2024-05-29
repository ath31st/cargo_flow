package ru.gnivc.common.role;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.Getter;

@Getter
public enum KeycloakRealmRoles {
  REALM_ADMIN(1, null),
  REGISTRATOR(2, null),
  ADMIN(3, "adminRole"),
  LOGIST(4, "logistRole"),
  DRIVER(5, "driverRole");

  KeycloakRealmRoles(int priority, String attributeName) {
    this.priority = priority;
    this.attributeName = attributeName;
  }

  private final int priority;
  private final String attributeName;

  public static List<String> getAttributeNames() {
    return Arrays.stream(KeycloakRealmRoles.values())
        .filter(Objects::nonNull)
        .map(KeycloakRealmRoles::getAttributeName)
        .toList();
  }
}
