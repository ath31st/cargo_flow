package ru.gnivc.portal.service;

import java.util.Optional;
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
    checkExistsVehicleByVin(req.vin(), req.licensePlate());

    CompanyVehicle cv = new CompanyVehicle();
    cv.setVin(req.vin().toUpperCase());
    cv.setLicensePlate(req.licensePlate().toUpperCase());
    cv.setYear(req.year());
    cv.setCompany(company);

    companyVehicleRepository.save(cv);
  }

  private void checkExistsVehicleByVin(String vin, String licensePlate) {
    if (companyVehicleRepository.existsByVinIgnoreCaseOrLicensePlateIgnoreCase(vin, licensePlate)) {
      throw new CompanyVehicleServiceException(HttpStatus.CONFLICT,
          String.format("Vehicle with this registration data: %s %s already exists",
              vin, licensePlate));
    }
  }

  public String getLicensePlate(String companyId, Integer vehicleId) {
    Optional<String> licensePlate = companyVehicleRepository.findLicensePlateByCompanyIdAndVehicleId(
        Integer.parseInt(companyId), vehicleId);
    return licensePlate.orElseThrow(
        () -> new CompanyVehicleServiceException(HttpStatus.NOT_FOUND,
            "Vehicle with this registration data not found"));
  }

  public void validateVehicleInCompany(Integer vehicleId, Integer companyId) {
    if (!companyVehicleRepository.existsByCompanyIdAndVehicleId(companyId, vehicleId)) {
      throw new CompanyVehicleServiceException(HttpStatus.NOT_FOUND,
          String.format("Vehicle with id: %d in company with id %d not found", vehicleId, companyId));
    }
  }
}
