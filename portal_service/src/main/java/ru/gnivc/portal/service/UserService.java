package ru.gnivc.portal.service;

import static ru.gnivc.common.constants.StringConstants.NOT_FOUND;
import static ru.gnivc.common.constants.StringConstants.USER_WITH_EMAIL;
import static ru.gnivc.common.constants.StringConstants.USER_WITH_ID;
import static ru.gnivc.common.role.KeycloakRealmRoles.REALM_ADMIN;
import static ru.gnivc.common.role.KeycloakRealmRoles.REGISTRATOR;

import jakarta.ws.rs.NotFoundException;
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
import ru.gnivc.common.exception.UserServiceException;
import ru.gnivc.common.role.KeycloakRealmRoles;
import ru.gnivc.portal.dto.user.EmployeeRegisterReq;
import ru.gnivc.portal.dto.user.IndividualRegisterReq;
import ru.gnivc.portal.dto.user.NewUserDataReq;
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
    user.setEmail(req.email().toLowerCase().trim());
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
          USER_WITH_ID.getValue() + userId + " mismatch with user founded by email");
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
      throw new UserServiceException(HttpStatus.NOT_FOUND,
          USER_WITH_EMAIL.getValue() + email + NOT_FOUND.getValue());
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
          HttpStatus.CONFLICT, USER_WITH_EMAIL.getValue() + email + " already exists");
    }
  }

  public void registerEmployee(EmployeeRegisterReq req, String companyId) {
    List<UserRepresentation> users = getUsersResource()
        .searchByEmail(req.email().toLowerCase().trim(), true);
    if (!users.isEmpty()) {
      String userId = users.get(0).getId();
      checkExistsEmployeeInCompany(userId, companyId);

      KeycloakRealmRoles role = KeycloakRealmRoles.valueOf(req.role());

      addRealmRoleToUser(userId, role.name());
      addAttributeRoleToUser(userId, role.getAttributeName(), companyId);
    } else {
      throw new UserServiceException(
          HttpStatus.NOT_FOUND, USER_WITH_EMAIL.getValue() + req.email() + NOT_FOUND.getValue());
    }
  }

  public List<String> getEmployeesByRole(String companyId, KeycloakRealmRoles role) {
    List<UserRepresentation> users = getUsersResource().searchByAttributes(role.getAttributeName());

    return users.stream()
        .filter(user -> hasRoleInCompany(user, role.getAttributeName(), companyId))
        .map(user -> user.getEmail() + " : " + role.name())
        .toList();
  }

  private boolean hasRoleInCompany(UserRepresentation user, String roleName, String companyId) {
    Map<String, List<String>> attributes = user.getAttributes();
    if (attributes != null && attributes.containsKey(roleName)) {
      return attributes.get(roleName).contains(companyId);
    }
    return false;
  }

  private void checkExistsEmployeeInCompany(String userId, String companyId) {
    UserRepresentation user = getUsersResource().get(userId).toRepresentation();

    Map<String, List<String>> attributes = user.getAttributes();
    if (attributes != null) {
      boolean companyIdFound = attributes.entrySet().stream()
          .filter(a -> KeycloakRealmRoles.getAttributeNames().contains(a.getKey()))
          .flatMap(a -> a.getValue().stream())
          .anyMatch(c -> c.equals(companyId));

      if (companyIdFound) {
        throw new UserServiceException(HttpStatus.CONFLICT,
            "Employee already belongs to the company");
      }
    }
  }

  public String getDriverFullName(String id) {
    final UserRepresentation user = getUserRepById(id);
    return user.getFirstName() + " " + user.getLastName();
  }

  public void validateDriverInCompany(String driverKeycloakId, String companyId) {
    UserRepresentation user = getUserRepById(driverKeycloakId);
    if (!hasRoleInCompany(user, KeycloakRealmRoles.DRIVER.getAttributeName(), companyId)) {
      throw new UserServiceException(HttpStatus.NOT_FOUND,
          String.format("Driver with id: %s in company: %s not found", driverKeycloakId, companyId));
    }
  }

  private UserRepresentation getUserRepById(String id) {
    UserResource userResource = getUsersResource().get(id);
    try {
      return userResource.toRepresentation();
    } catch (NotFoundException e) {
      throw new UserServiceException(HttpStatus.NOT_FOUND,
          USER_WITH_ID.getValue() + id + NOT_FOUND.getValue());
    }
  }
}