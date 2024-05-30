package ru.gnivc.common.validator;

import static ru.gnivc.common.role.KeycloakRealmRoles.ADMIN;
import static ru.gnivc.common.role.KeycloakRealmRoles.LOGIST;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import ru.gnivc.common.exception.RealmRoleException;
import ru.gnivc.common.role.KeycloakRealmRoles;
import ru.gnivc.common.role.RoleExtractor;

public class PermissionValidator {

  private Jwt getCurrentPrincipal() {
    return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  public boolean hasAdminOrLogistAccess(String companyId) {
    Jwt principal = getCurrentPrincipal();
    final Optional<KeycloakRealmRoles> role = RoleExtractor.findInAttributes(principal, companyId);
    return role.isPresent() && (role.get() == ADMIN || role.get() == LOGIST);
  }

  public boolean hasAdminAccess(String companyId) {
    Jwt principal = getCurrentPrincipal();
    final Optional<KeycloakRealmRoles> role = RoleExtractor.findInAttributes(principal, companyId);
    return role.isPresent() && role.get() == ADMIN;
  }

  public boolean canRegisterEmployee(String companyId, String employeeRole) {
    Jwt principal = getCurrentPrincipal();
    final Optional<KeycloakRealmRoles> role = RoleExtractor.findInAttributes(principal, companyId);
    if (role.isPresent() && (role.get() == ADMIN || role.get() == LOGIST)) {
      try {
        KeycloakRealmRoles employeeRealmRole = KeycloakRealmRoles.valueOf(employeeRole);
        return role.get().getPriority() <= employeeRealmRole.getPriority();
      } catch (IllegalArgumentException e) {
        throw new RealmRoleException(HttpStatus.BAD_REQUEST,
            "Invalid employee role: " + employeeRole);
      }
    } else {
      return false;
    }
  }
}
