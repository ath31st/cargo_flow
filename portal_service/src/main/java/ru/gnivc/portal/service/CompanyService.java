package ru.gnivc.portal.service;

import static ru.gnivc.common.role.KeycloakAttributeRoles.ADMIN_ROLE;
import static ru.gnivc.common.role.KeycloakRealmRoles.ADMIN;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gnivc.portal.dto.user.DadataCompany;
import ru.gnivc.portal.entity.Company;
import ru.gnivc.common.exception.CompanyServiceException;
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
      userService.addAttributeRoleToUser(userId, ADMIN_ROLE.getRoleName(),
          newCompany.getId().toString());
    } catch (Exception e) {
      if (newCompany != null) {
        userService.removeRealmRoleFromUser(userId, ADMIN.name());
        userService.removeAttributeRoleFromUser(userId, ADMIN_ROLE.getRoleName(),
            newCompany.getId().toString());
      }
      throw new CompanyServiceException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  private void checkCompanyExists(String companyInn) {
    if (companyRepository.existsByInn(companyInn)) {
      throw new CompanyServiceException(HttpStatus.CONFLICT,
          "Company with inn: " + companyInn + " already exists");
    }
  }
}
