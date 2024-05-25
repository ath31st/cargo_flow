package ru.gnivc.portal.service;

import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.gnivc.portal.dto.IndividualRegisterReq;
import ru.gnivc.portal.util.PasswordGenerator;

@Service
@RequiredArgsConstructor
public class KeycloakService {

  private final Keycloak keycloak;

  @Value("${keycloak.realm}")
  private String realm;

  public List<String> getRealmRoles() {
    return keycloak
        .realm(realm)
        .roles()
        .list()
        .stream()
        .map(RoleRepresentation::getName)
        .toList();
  }

  public String registerIndividual(IndividualRegisterReq req) {
    final String randomPassword = generatePassword();

    String username = req.email().split("@")[0];
    CredentialRepresentation credential = createPasswordCredentials(randomPassword);
    UserRepresentation user = new UserRepresentation();
    user.setUsername(username);
    user.setFirstName(req.firstName());
    user.setLastName(req.lastName());
    user.setEmail(req.email());
    user.setCredentials(Collections.singletonList(credential));
    user.setEnabled(true);
    user.setEmailVerified(true);

    try (Response response = getUsersResource().create(user)) {
      if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
        addRealmRoleToUser(username, "REGISTRATOR");
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return randomPassword;
  }

  private static String generatePassword() {
    return new PasswordGenerator.Builder()
        .lower(1)
        .upper(1)
        .generate(6);
  }

  public void addRealmRoleToUser(String username, String roleName) {
    List<UserRepresentation> users = getUsersResource().search(username);

    UserRepresentation user = users.get(0);
    String userId = user.getId();

    RoleRepresentation role = keycloak.realm(realm)
        .roles()
        .get(roleName)
        .toRepresentation();

    UserResource userResource = getUsersResource().get(userId);
    userResource.roles()
        .realmLevel()
        .add(Collections.singletonList(role));
  }

  private UsersResource getUsersResource() {
    return keycloak.realm(realm).users();
  }

  private static CredentialRepresentation createPasswordCredentials(String password) {
    CredentialRepresentation passwordCredentials = new CredentialRepresentation();
    passwordCredentials.setTemporary(false);
    passwordCredentials.setType(CredentialRepresentation.PASSWORD);
    passwordCredentials.setValue(password);
    return passwordCredentials;
  }
}