package ru.gnivc.portal.controller;

import static ru.gnivc.common.role.KeycloakRoles.ADMIN;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gnivc.portal.entity.Company;
import ru.gnivc.portal.service.CompanyService;
import ru.gnivc.portal.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/portal/v1/companies")
public class CompanyController {
  private final CompanyService companyService;
  private final UserService userService;

  @PreAuthorize("#principal.getClaimAsStringList('adminRole').contains(#companyId)")
  @PostMapping("/some-manipulation-with-company/{companyId}")
  public ResponseEntity<HttpStatus> manipulationWithCompany(
      @AuthenticationPrincipal Jwt principal, @PathVariable String companyId) {

    return ResponseEntity.ok().body(HttpStatus.OK);
  }

  @PostMapping("/register-company/{company-inn}")
  public ResponseEntity<HttpStatus> registerCompany(@PathVariable("company-inn") String companyInn,
                                                    @AuthenticationPrincipal Jwt principal) {
    companyService.checkCompanyExists(companyInn);
    try {
      final Company newCompany = companyService.createNewCompany(companyInn);
      userService.addRealmRoleToUser(principal.getClaim("preferred_username"), ADMIN.name());
      userService.addAttributeRoleToUser(principal.getSubject(), "adminRole", newCompany.getId().toString());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return ResponseEntity.ok().body(HttpStatus.CREATED);
  }
}
