package ru.gnivc.portal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.gnivc.portal.dto.user.DadataCompany;
import ru.gnivc.portal.entity.Company;
import ru.gnivc.portal.exception.CompanyServiceException;
import ru.gnivc.portal.repository.CompanyRepository;

@Service
@RequiredArgsConstructor
public class CompanyService {
  private final CompanyRepository companyRepository;
  private final DadataService dadataService;

  public Company createNewCompany(String companyInn) {
    final DadataCompany dadataCompany = dadataService.getDadataCompany(companyInn);

    Company company = new Company();
    company.setName(dadataCompany.name());
    company.setInn(dadataCompany.inn());
    company.setAddress(dadataCompany.address());
    company.setKpp(dadataCompany.kpp());
    company.setOgrn(dadataCompany.ogrn());

    return companyRepository.save(company);
  }

  public void checkCompanyExists(String companyInn) {
    if (companyRepository.existsByInn(companyInn)) {
      throw new CompanyServiceException(HttpStatus.CONFLICT,
          "Company with inn: " + companyInn + " already exists");
    }
  }
}
