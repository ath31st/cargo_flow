package ru.gnivc.portal.service;

import static ru.gnivc.common.role.KeycloakRealmRoles.ADMIN;
import static ru.gnivc.common.role.KeycloakRealmRoles.DRIVER;
import static ru.gnivc.common.role.KeycloakRealmRoles.LOGIST;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gnivc.common.exception.CompanyServiceException;
import ru.gnivc.portal.dto.company.CompanyDto;
import ru.gnivc.portal.dto.company.CompanyShortDto;
import ru.gnivc.portal.dto.company.DadataCompany;
import ru.gnivc.portal.entity.Company;
import ru.gnivc.portal.repository.CompanyRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {
  private final CompanyRepository companyRepository;
  private final UserService userService;
  private final DadataService dadataService;

  public Company createCompany(String companyInn) {
    final DadataCompany dadataCompany = dadataService.getDadataCompany(companyInn);

    Company company = new Company();
    company.setName(dadataCompany.name());
    company.setInn(dadataCompany.inn());
    company.setAddress(dadataCompany.address());
    company.setKpp(dadataCompany.kpp());
    company.setOgrn(dadataCompany.ogrn());

    return companyRepository.save(company);
  }

  @Transactional
  public void registerCompany(String companyInn, String userId) {
    checkCompanyExists(companyInn);

    Company newCompany = createCompany(companyInn);

    try {
      userService.addRealmRoleToUser(userId, ADMIN.name());
      userService.addAttributeRoleToUser(userId, ADMIN.getAttributeName(),
          newCompany.getId().toString());
    } catch (Exception e) {
      if (newCompany != null) {
        userService.removeRealmRoleFromUser(userId, ADMIN.name());
        userService.removeAttributeRoleFromUser(userId, ADMIN.getAttributeName(),
            newCompany.getId().toString());
      }
      throw new CompanyServiceException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  public Company getCompany(String companyId) {
    return companyRepository.findById(Integer.parseInt(companyId))
        .orElseThrow(() -> new CompanyServiceException(HttpStatus.NOT_FOUND, "Company not found"));
  }

  public CompanyDto getCompanyInfo(String companyId) {
    Company company = getCompany(companyId);

    long logistCount = userService.getEmployeesByRole(companyId, LOGIST).size();
    long driverCount = userService.getEmployeesByRole(companyId, DRIVER).size();

    return CompanyDto.builder()
        .id(company.getId())
        .name(company.getName())
        .inn(company.getInn())
        .address(company.getAddress())
        .kpp(company.getKpp())
        .ogrn(company.getOgrn())
        .quantityLogists(logistCount)
        .quantityDrivers(driverCount)
        .build();
  }

  public List<Integer> getCompanyIds() {
    return companyRepository.findAllCompanyIds();
  }

  public Page<CompanyShortDto> getCompanies(Pageable pageable) {
    return companyRepository.findAll(pageable)
        .map(c -> new CompanyShortDto(c.getId(), c.getName(), c.getInn()));
  }

  public List<String> getEmployees(String companyId) {
    final List<String> admins = userService.getEmployeesByRole(companyId, ADMIN);
    final List<String> logists = userService.getEmployeesByRole(companyId, LOGIST);
    final List<String> drivers = userService.getEmployeesByRole(companyId, DRIVER);

    List<String> allEmployees = new ArrayList<>();
    allEmployees.addAll(admins);
    allEmployees.addAll(logists);
    allEmployees.addAll(drivers);

    return allEmployees;
  }

  private void checkCompanyExists(String companyInn) {
    if (companyRepository.existsByInn(companyInn)) {
      throw new CompanyServiceException(HttpStatus.CONFLICT,
          "Company with inn: " + companyInn + " already exists");
    }
  }
}
