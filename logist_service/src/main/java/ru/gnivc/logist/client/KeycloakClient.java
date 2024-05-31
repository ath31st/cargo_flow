package ru.gnivc.logist.client;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.gnivc.common.exception.UserServiceException;

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

  private UserRepresentation getUserRepById(String id) {
    UserResource userResource = getUsersResource().get(id);
    try {
      return userResource.toRepresentation();
    } catch (NotFoundException e) {
      throw new UserServiceException(HttpStatus.NOT_FOUND, "User with id: " + id + " not found");
    }
  }

  private UsersResource getUsersResource() {
    return keycloak.realm(realm).users();
  }
}
