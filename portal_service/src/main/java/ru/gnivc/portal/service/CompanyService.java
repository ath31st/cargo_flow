package ru.gnivc.portal.service;

import static ru.gnivc.common.role.KeycloakRealmRoles.ADMIN;
import static ru.gnivc.common.role.KeycloakRealmRoles.DRIVER;
import static ru.gnivc.common.role.KeycloakRealmRoles.LOGIST;

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

  public CompanyDto getCompanyInfo(String companyId) {
    Company company = companyRepository.findById(Integer.parseInt(companyId))
        .orElseThrow(() -> new CompanyServiceException(HttpStatus.NOT_FOUND, "Company not found"));

    long logistCount = userService.getEmployeesByRole(companyId, LOGIST).size();
    long driverCount = userService.getEmployeesByRole(companyId, DRIVER).size();

    return new CompanyDto(company.getId(), company.getName(), company.getInn(),
        company.getAddress(), company.getKpp(), company.getOgrn(), logistCount, driverCount);
  }


  public Page<CompanyShortDto> getCompanies(Pageable pageable) {
    return companyRepository.findAll(pageable)
        .map(c -> new CompanyShortDto(c.getId(), c.getName(), c.getInn()));
  }

  private void checkCompanyExists(String companyInn) {
    if (companyRepository.existsByInn(companyInn)) {
      throw new CompanyServiceException(HttpStatus.CONFLICT,
          "Company with inn: " + companyInn + " already exists");
    }
  }
}
