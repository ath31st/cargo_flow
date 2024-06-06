package ru.gnivc.common.validator;

import static ru.gnivc.common.role.KeycloakRealmRoles.ADMIN;
import static ru.gnivc.common.role.KeycloakRealmRoles.DRIVER;
import static ru.gnivc.common.role.KeycloakRealmRoles.LOGIST;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.gnivc.common.exception.RealmRoleException;
import ru.gnivc.common.role.KeycloakRealmRoles;
import ru.gnivc.common.role.RoleExtractor;
import ru.gnivc.common.service.ServiceNames;

public class PermissionValidator {

  private Jwt getCurrentPrincipal() {
    return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  private Optional<HttpServletRequest> getCurrentHttpRequest() {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    return attributes != null ? Optional.of(attributes.getRequest()) : Optional.empty();
  }

  public boolean hasAccessByRolesOrServices(String companyId,
                                            Set<KeycloakRealmRoles> roles,
                                            Set<ServiceNames> services) {
    Jwt principal = getCurrentPrincipal();
    final Optional<KeycloakRealmRoles> role = RoleExtractor.findInAttributes(principal, companyId);
    final Optional<HttpServletRequest> request = getCurrentHttpRequest();
    if (request.isPresent() && request.get().getHeader("X-Service-name") != null) {
      for (ServiceNames service : services) {
        if (service.name().equals(request.get().getHeader("X-Service-name"))) {
          return true;
        }
      }
    }
    return role.isPresent() && roles.contains(role.get());
  }

  public boolean hasCompanyAdminOrLogistAccess(String companyId) {
    Jwt principal = getCurrentPrincipal();
    final Optional<KeycloakRealmRoles> role = RoleExtractor.findInAttributes(principal, companyId);
    return role.isPresent() && (role.get() == ADMIN || role.get() == LOGIST);
  }

  public boolean hasCompanyLogistAccess(String companyId) {
    Jwt principal = getCurrentPrincipal();
    final Optional<KeycloakRealmRoles> role = RoleExtractor.findInAttributes(principal, companyId);
    return role.isPresent() && role.get() == LOGIST;
  }

  public boolean hasCompanyDriverAccess(String companyId) {
    Jwt principal = getCurrentPrincipal();
    final Optional<KeycloakRealmRoles> role = RoleExtractor.findInAttributes(principal, companyId);
    return role.isPresent() && role.get() == DRIVER;
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
