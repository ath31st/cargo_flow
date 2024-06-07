package ru.gnivc.common.validator;

import java.util.Set;
import ru.gnivc.common.role.KeycloakRealmRoles;
import ru.gnivc.common.service.ServiceNames;

public record PermissionSet(Set<KeycloakRealmRoles> roles, Set<ServiceNames> services) {
}
