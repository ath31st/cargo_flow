package ru.gnivc.portal.controller;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.gnivc.portal.dto.company.CompanyDto;
import ru.gnivc.portal.dto.company.CompanyShortDto;
import ru.gnivc.portal.dto.user.EmployeeRegisterReq;
import ru.gnivc.portal.dto.vehicle.NewVehicleRegisterReq;
import ru.gnivc.portal.entity.Company;
import ru.gnivc.portal.service.CompanyService;
import ru.gnivc.portal.service.CompanyVehicleService;
import ru.gnivc.portal.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/portal/v1/companies")
public class CompanyController {
  private final CompanyService companyService;
  private final UserService userService;
  private final CompanyVehicleService companyVehicleService;

  @PostMapping("/register-company/{company-inn}")
  public ResponseEntity<HttpStatus> registerCompany(@PathVariable("company-inn") String companyInn,
                                                    @AuthenticationPrincipal Jwt principal) {
    companyService.registerCompany(companyInn, principal.getSubject());
    return ResponseEntity.ok().body(HttpStatus.CREATED);
  }

  @PreAuthorize("@permissionValidator.canRegisterEmployee(#companyId, #req.role())")
  @PostMapping("/{companyId}/register-employee")
  public ResponseEntity<HttpStatus> registerEmployee(@PathVariable String companyId,
                                                     @RequestBody EmployeeRegisterReq req) {
    userService.registerEmployee(req, companyId);
    return ResponseEntity.ok().build();
  }

  @Transactional
  @PreAuthorize("@permissionValidator.hasCompanyAdminOrLogistAccess(#companyId)")
  @PostMapping("/{companyId}/register-vehicle")
  public ResponseEntity<HttpStatus> registerVehicle(@PathVariable String companyId,
                                                    @RequestBody NewVehicleRegisterReq req) {
    Company company = companyService.getCompany(companyId);
    companyVehicleService.registerVehicle(req, company);
    return ResponseEntity.ok().body(HttpStatus.CREATED);
  }

  @GetMapping("/{companyId}")
  public ResponseEntity<CompanyDto> companyInfo(@PathVariable String companyId) {
    return ResponseEntity.ok().body(companyService.getCompanyInfo(companyId));
  }

  @GetMapping("/all")
  public ResponseEntity<Page<CompanyShortDto>> allCompanies(
      @RequestParam(defaultValue = "0") Integer pageNumber,
      @RequestParam(defaultValue = "10") Integer pageSize) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    return ResponseEntity.ok().body(companyService.getCompanies(pageable));
  }

  @GetMapping("/{companyId}/employees")
  public ResponseEntity<List<String>> companyEmployees(@PathVariable String companyId) {
    return ResponseEntity.ok().body(companyService.getEmployees(companyId));
  }

  @PreAuthorize("@permissionValidator.hasCompanyLogistAccess(#companyId)")
  @GetMapping("/{companyId}/vehicles/{vehicleId}/license-plate")
  public ResponseEntity<String> getCompanyVehicleLicensePlate(
      @PathVariable String companyId, @PathVariable Integer vehicleId) {
    return ResponseEntity.ok().body(companyVehicleService.getLicensePlate(companyId, vehicleId));
  }

  @PreAuthorize("@permissionValidator.hasCompanyLogistAccess(#companyId)")
  @GetMapping("/{companyId}/drivers/{driverId}/full-name")
  public ResponseEntity<String> getCompanyDriverFullName(
      @PathVariable String companyId, @PathVariable String driverId) {
    userService.validateDriverInCompany(driverId, companyId);
    return ResponseEntity.ok().body(userService.getDriverFullName(driverId));
  }

  @PreAuthorize("@permissionValidator.hasCompanyLogistAccess(#companyId)")
  @GetMapping("/{companyId}/drivers/{driverId}/validate")
  public ResponseEntity<HttpStatus> validateDriverInCompany(
      @PathVariable String companyId, @PathVariable String driverId) {
    userService.validateDriverInCompany(driverId, companyId);
    return ResponseEntity.ok().build();
  }

  @PreAuthorize("@permissionValidator.hasCompanyLogistAccess(#companyId)")
  @GetMapping("/{companyId}/vehicles/{vehicleId}/validate")
  public ResponseEntity<HttpStatus> validateVehicleInCompany(
      @PathVariable Integer companyId, @PathVariable Integer vehicleId) {
    companyVehicleService.validateVehicleInCompany(vehicleId, companyId);
    return ResponseEntity.ok().build();
  }
}
