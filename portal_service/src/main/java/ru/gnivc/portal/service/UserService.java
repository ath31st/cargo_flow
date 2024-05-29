package ru.gnivc.portal.service;

import static ru.gnivc.common.role.KeycloakRealmRoles.REALM_ADMIN;
import static ru.gnivc.common.role.KeycloakRealmRoles.REGISTRATOR;

import jakarta.ws.rs.core.Response;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.gnivc.portal.dto.user.IndividualRegisterReq;
import ru.gnivc.portal.dto.user.NewUserDataReq;
import ru.gnivc.portal.exception.UserServiceException;
import ru.gnivc.portal.util.PasswordGenerator;

@Service
@RequiredArgsConstructor
public class UserService {

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
        .filter(r -> !r.equals(REGISTRATOR.name())
            && !r.equals(REALM_ADMIN.name())
            && r.equals(r.toUpperCase()))
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
        List<UserRepresentation> users = getUsersResource().search(username);
        UserRepresentation ur = users.get(0);
        String userId = ur.getId();
        addRealmRoleToUser(userId, REGISTRATOR.name());
      }
    } catch (Exception e) {
      throw new UserServiceException(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    return randomPassword;
  }

  public void addRealmRoleToUser(String userId, String roleName) {
    RoleRepresentation role = keycloak.realm(realm)
        .roles()
        .get(roleName)
        .toRepresentation();

    UserResource userResource = getUsersResource().get(userId);
    userResource.roles()
        .realmLevel()
        .add(Collections.singletonList(role));
  }

  public void removeRealmRoleFromUser(String userId, String roleName) {
    RoleRepresentation role = keycloak.realm(realm)
        .roles()
        .get(roleName)
        .toRepresentation();

    UserResource userResource = getUsersResource().get(userId);
    userResource.roles()
        .realmLevel()
        .remove(Collections.singletonList(role));
  }

  public void addAttributeRoleToUser(String userId, String attributeName, String companyId) {
    UserRepresentation user = getUsersResource().get(userId).toRepresentation();

    Map<String, List<String>> attributes = user.getAttributes();
    if (attributes == null) {
      attributes = new HashMap<>();
    }

    List<String> companyIds = attributes.getOrDefault(attributeName, new ArrayList<>());
    if (!companyIds.contains(companyId)) {
      companyIds.add(companyId);
    }
    attributes.put(attributeName, companyIds);
    user.setAttributes(attributes);

    UserResource userResource = getUsersResource().get(userId);
    userResource.update(user);
  }

  public void removeAttributeRoleFromUser(String userId, String attributeName, String companyId) {
    UserRepresentation user = getUsersResource().get(userId).toRepresentation();

    Map<String, List<String>> attributes = user.getAttributes();
    if (attributes != null && attributes.containsKey(attributeName)) {
      List<String> companyIds = attributes.get(attributeName);
      if (companyIds != null && companyIds.contains(companyId)) {
        companyIds.remove(companyId);
        if (companyIds.isEmpty()) {
          attributes.remove(attributeName);
        } else {
          attributes.put(attributeName, companyIds);
        }
        user.setAttributes(attributes);

        UserResource userResource = getUsersResource().get(userId);
        userResource.update(user);
      }
    }
  }


  public void changeData(Principal principal, NewUserDataReq req) {
    UserResource userResource = getUsersResource().get(principal.getName());
    UserRepresentation user = userResource.toRepresentation();

    if (req.newFirstName() != null && !req.newFirstName().isEmpty()) {
      user.setFirstName(req.newFirstName());
    }
    if (req.newLastName() != null && !req.newLastName().isEmpty()) {
      user.setLastName(req.newLastName());
    }
    if (req.newEmail() != null && !req.newEmail().isEmpty()) {
      user.setEmail(req.newEmail());
    }

    userResource.update(user);
  }

  public String changePassword(String userId, String email, String newPassword) {
    if (newPassword == null || newPassword.isEmpty()) {
      newPassword = generatePassword();
    }

    UserRepresentation userRepByEmail = getUserRepByEmail(email);
    String id = userRepByEmail.getId();

    if (!id.equals(userId)) {
      throw new UserServiceException(HttpStatus.BAD_REQUEST,
          "User with id " + userId + " mismatch with user founded by email");
    }

    CredentialRepresentation newCredentials = createPasswordCredentials(newPassword);
    getUsersResource().get(userId).resetPassword(newCredentials);

    return newPassword;
  }


  private static String generatePassword() {
    return new PasswordGenerator.Builder()
        .lower(1)
        .upper(1)
        .generate(6);
  }

  private UsersResource getUsersResource() {
    return keycloak.realm(realm).users();
  }

  private UserRepresentation getUserRepByEmail(String email) {
    List<UserRepresentation> users = getUsersResource().searchByEmail(email, true);
    if (users.isEmpty()) {
      throw new UserServiceException(HttpStatus.NOT_FOUND, "User with email " + email + " not found");
    }
    return users.get(0);
  }

  private static CredentialRepresentation createPasswordCredentials(String password) {
    CredentialRepresentation passwordCredentials = new CredentialRepresentation();
    passwordCredentials.setTemporary(false);
    passwordCredentials.setType(CredentialRepresentation.PASSWORD);
    passwordCredentials.setValue(password);
    return passwordCredentials;
  }

  public void checkExistsUserByEmail(String email) {
    boolean exact = true;
    List<UserRepresentation> users = getUsersResource()
        .searchByEmail(email.toLowerCase().trim(), exact);
    if (!users.isEmpty()) {
      throw new UserServiceException(
          HttpStatus.CONFLICT, "User with email " + email + " already exists");
    }
  }
}