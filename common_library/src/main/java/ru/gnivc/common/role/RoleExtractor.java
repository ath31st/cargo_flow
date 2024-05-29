package ru.gnivc.common.role;

import static ru.gnivc.common.role.KeycloakAttributeRoles.ADMIN_ROLE;
import static ru.gnivc.common.role.KeycloakAttributeRoles.DRIVER_ROLE;
import static ru.gnivc.common.role.KeycloakAttributeRoles.LOGIST_ROLE;

import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.springframework.security.oauth2.jwt.Jwt;

@UtilityClass
public class RoleExtractor {

  public static Optional<KeycloakRealmRoles> findInAttributes(Jwt principal, String companyId) {
    Optional<KeycloakRealmRoles> role = Optional.empty();

    if (principal.getClaimAsStringList(ADMIN_ROLE.getRoleName()) != null
        && principal.getClaimAsStringList(ADMIN_ROLE.getRoleName()).contains(companyId)) {
      role = Optional.of(KeycloakRealmRoles.ADMIN);
    } else if (principal.getClaimAsStringList(LOGIST_ROLE.getRoleName()) != null
        && principal.getClaimAsStringList(LOGIST_ROLE.getRoleName()).contains(companyId)) {
      role = Optional.of(KeycloakRealmRoles.LOGIST);
    } else if (principal.getClaimAsStringList(DRIVER_ROLE.getRoleName()) != null
        && principal.getClaimAsStringList(DRIVER_ROLE.getRoleName()).contains(companyId)) {
      role = Optional.of(KeycloakRealmRoles.DRIVER);
    }

    return role;
  }
}
