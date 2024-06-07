package ru.gnivc.starter;

import java.util.Collections;
import java.util.Set;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.gnivc.common.role.KeycloakRealmRoles;
import ru.gnivc.common.service.ServiceNames;
import ru.gnivc.common.validator.PermissionSet;
import ru.gnivc.common.validator.PermissionValidator;

@Configuration
@ConditionalOnClass(PermissionValidator.class)
public class PermissionValidatorAutoConfiguration {

  @Bean
  public PermissionValidator permissionValidator() {
    return new PermissionValidator();
  }

  @Bean
  public PermissionSet logistLogistService() {
    Set<KeycloakRealmRoles> roles = Set.of(KeycloakRealmRoles.LOGIST);
    Set<ServiceNames> services = Set.of(ServiceNames.LOGIST_SERVICE);
    return new PermissionSet(roles, services);
  }

  @Bean
  public PermissionSet logistDriverService() {
    Set<KeycloakRealmRoles> roles = Set.of(KeycloakRealmRoles.LOGIST);
    Set<ServiceNames> services = Set.of(ServiceNames.DRIVER_SERVICE);
    return new PermissionSet(roles, services);
  }

  @Bean
  public PermissionSet adminLogist() {
    Set<KeycloakRealmRoles> roles = Set.of(KeycloakRealmRoles.LOGIST, KeycloakRealmRoles.ADMIN);
    return new PermissionSet(roles, Collections.emptySet());
  }
}
