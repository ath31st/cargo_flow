package ru.gnivc.common.role;

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
}
