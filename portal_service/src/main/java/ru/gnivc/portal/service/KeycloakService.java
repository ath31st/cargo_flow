package ru.gnivc.portal.service;

import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
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

  public void registerIndividual(IndividualRegisterReq req) {
    final String randomPassword = generatePassword();

    String username = req.email();
    CredentialRepresentation credential = createPasswordCredentials(randomPassword);
    UserRepresentation user = new UserRepresentation();
    user.setUsername(username);
    user.setEmail(username);
    user.setCredentials(Collections.singletonList(credential));
    user.setEnabled(true);

    UsersResource usersResource = getUsersResource();

    try (Response response = usersResource.create(user)) {
      if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
        addRealmRoleToUser(username, "REGISTRATOR");
      }
    }
  }

  private static String generatePassword() {
    return new PasswordGenerator.Builder()
        .digits(1)
        .lower(1)
        .upper(1)
        .punctuation().custom("-._")
        .generate(8);
  }

  private void addRealmRoleToUser(String userName, String roleName) {
    RealmResource realmResource = keycloak.realm(realm);
    List<UserRepresentation> users = realmResource.users().search(userName);
    UserResource userResource = realmResource.users().get(users.get(0).getId());
    RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
    RoleMappingResource roleMappingResource = userResource.roles();
    roleMappingResource.realmLevel().add(Collections.singletonList(role));
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