package ru.gnivc.logist.client;

import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.gnivc.common.exception.KeycloakClientException;
import ru.gnivc.common.role.KeycloakRealmRoles;

@Service
@RequiredArgsConstructor
public class KeycloakClient {
  @Value("${keycloak.realm}")
  private String realm;

  private final Keycloak keycloak;

  public String getUserFullName(String id) {
    final UserRepresentation user = getUserRepById(id);
    return user.getFirstName() + " " + user.getLastName();
  }

  public void validateDriverInCompany(String id, String companyId) {
    UserRepresentation user = getUserRepById(id);
    if (!hasRoleInCompany(user, KeycloakRealmRoles.DRIVER.getAttributeName(), companyId)) {
      throw new KeycloakClientException(HttpStatus.NOT_FOUND,
          String.format("Driver with id: %s in company: %s not found", id, companyId));
    }
  }

  private boolean hasRoleInCompany(UserRepresentation user, String roleName, String companyId) {
    Map<String, List<String>> attributes = user.getAttributes();
    if (attributes != null && attributes.containsKey(roleName)) {
      return attributes.get(roleName).contains(companyId);
    }
    return false;
  }

  private UserRepresentation getUserRepById(String id) {
    UserResource userResource = getUsersResource().get(id);
    try {
      return userResource.toRepresentation();
    } catch (NotFoundException e) {
      throw new KeycloakClientException(HttpStatus.NOT_FOUND, "User with id: " + id + " not found");
    }
  }

  private UsersResource getUsersResource() {
    return keycloak.realm(realm).users();
  }
}
