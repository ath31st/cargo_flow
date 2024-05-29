package ru.gnivc.common.role;

public enum KeycloakRealmRoles {
  REALM_ADMIN(1),
  REGISTRATOR(2),
  ADMIN(3),
  LOGIST(4),
  DRIVER(5);

  KeycloakRealmRoles(int priority) {
    this.priority = priority;
  }

  private final int priority;

  public int getPriority() {
    return priority;
  }
}
