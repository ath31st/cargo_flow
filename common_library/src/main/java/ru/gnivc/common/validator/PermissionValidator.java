package ru.gnivc.common.validator;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import ru.gnivc.common.exception.RealmRoleException;
import ru.gnivc.common.role.KeycloakRealmRoles;
import ru.gnivc.common.role.RoleExtractor;

public class PermissionValidator {

  public boolean validateForPortalAccess(Jwt principal, String companyId) {
    final Optional<KeycloakRealmRoles> role = RoleExtractor.findInAttributes(principal, companyId);
    return role.isPresent();
  }

  public boolean validateForRegisterEmployee(Jwt principal, String companyId, String employeeRole) {
    final Optional<KeycloakRealmRoles> role = RoleExtractor.findInAttributes(principal, companyId);
    if (role.isPresent()) {
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
