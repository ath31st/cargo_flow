package ru.gnivc.portal.service;

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

//  public void addUser(UserRegisterDto dto) {
//    String username = dto.getUsername();
//    CredentialRepresentation credential = createPasswordCredentials(dto.getPassword());
//    UserRepresentation user = new UserRepresentation();
//    user.setUsername(username);
//    user.setCredentials(Collections.singletonList(credential));
//    user.setEnabled(true);
//    UsersResource usersResource = getUsersResource();
//    usersResource.create(user);
//    addRealmRoleToUser(username, dto.getRole());
//  }

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