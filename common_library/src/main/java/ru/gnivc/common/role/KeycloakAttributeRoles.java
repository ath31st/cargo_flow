package ru.gnivc.common.role;

public enum KeycloakAttributeRoles {
  ADMIN_ROLE("adminRole"),
  DRIVER_ROLE("driverRole"),
  LOGIST_ROLE("logistRole");

  KeycloakAttributeRoles(String roleName) {
    this.roleName = roleName;
  }

  private final String roleName;

  public String getRoleName() {
    return roleName;
  }
}
