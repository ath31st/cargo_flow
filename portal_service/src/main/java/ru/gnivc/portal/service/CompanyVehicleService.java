package ru.gnivc.portal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.gnivc.common.exception.CompanyVehicleServiceException;
import ru.gnivc.portal.dto.vehicle.NewVehicleRegisterReq;
import ru.gnivc.portal.entity.Company;
import ru.gnivc.portal.entity.CompanyVehicle;
import ru.gnivc.portal.repository.CompanyVehicleRepository;

@Service
@RequiredArgsConstructor
public class CompanyVehicleService {
  private final CompanyVehicleRepository companyVehicleRepository;

  public void registerVehicle(NewVehicleRegisterReq req, Company company) {
    checkExistsVehicleByVin(req.vin());

    CompanyVehicle cv = new CompanyVehicle();
    cv.setVin(req.vin());
    cv.setYear(req.year());
    cv.setCompany(company);

    companyVehicleRepository.save(cv);
  }

  private void checkExistsVehicleByVin(String vin) {
    if (companyVehicleRepository.existsByVin(vin)) {
      throw new CompanyVehicleServiceException(HttpStatus.CONFLICT,
          "Vehicle with VIN: " + vin + " already exists");
    }
  }
}
