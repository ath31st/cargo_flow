package ru.gnivc.portal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gnivc.portal.dto.user.EmployeeRegisterReq;
import ru.gnivc.portal.service.CompanyService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/portal/v1/companies")
public class CompanyController {
  private final CompanyService companyService;

  @PreAuthorize("#principal.getClaimAsStringList('adminRole').contains(#companyId)")
  @PostMapping("/some-manipulation-with-company/{companyId}")
  public ResponseEntity<HttpStatus> manipulationWithCompany(
      @AuthenticationPrincipal Jwt principal, @PathVariable String companyId) {

    return ResponseEntity.ok().body(HttpStatus.OK);
  }

  @PostMapping("/register-company/{company-inn}")
  public ResponseEntity<HttpStatus> registerCompany(@PathVariable("company-inn") String companyInn,
                                                    @AuthenticationPrincipal Jwt principal) {
    companyService.registerCompany(companyInn, principal.getSubject());

    return ResponseEntity.ok().body(HttpStatus.CREATED);
  }

  @PreAuthorize("@permissionValidator.validateForRegisterEmployee(#principal, #companyId, #req.role())")
  @PostMapping("/{companyId}/register-employee")
  public ResponseEntity<String> registerEmployee(@AuthenticationPrincipal Jwt principal,
                                                 @PathVariable Integer companyId,
                                                 @RequestBody EmployeeRegisterReq req) {

    return ResponseEntity.ok("TEST REGISTER EMPLOYEE");
  }
}
