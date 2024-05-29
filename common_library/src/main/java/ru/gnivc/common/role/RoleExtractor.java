package ru.gnivc.common.role;


import static ru.gnivc.common.role.KeycloakRealmRoles.ADMIN;
import static ru.gnivc.common.role.KeycloakRealmRoles.DRIVER;
import static ru.gnivc.common.role.KeycloakRealmRoles.LOGIST;

import java.util.Optional;
import lombok.experimental.UtilityClass;
import org.springframework.security.oauth2.jwt.Jwt;

@UtilityClass
public class RoleExtractor {

  public static Optional<KeycloakRealmRoles> findInAttributes(Jwt principal, String companyId) {
    Optional<KeycloakRealmRoles> role = Optional.empty();

    if (principal.getClaimAsStringList(ADMIN.getAttributeName()) != null
        && principal.getClaimAsStringList(ADMIN.getAttributeName()).contains(companyId)) {
      role = Optional.of(ADMIN);
    } else if (principal.getClaimAsStringList(LOGIST.getAttributeName()) != null
        && principal.getClaimAsStringList(LOGIST.getAttributeName()).contains(companyId)) {
      role = Optional.of(LOGIST);
    } else if (principal.getClaimAsStringList(DRIVER.getAttributeName()) != null
        && principal.getClaimAsStringList(DRIVER.getAttributeName()).contains(companyId)) {
      role = Optional.of(DRIVER);
    }

    return role;
  }
}
