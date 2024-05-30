package ru.gnivc.portal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gnivc.portal.dto.vehicle.NewVehicleRegisterReq;
import ru.gnivc.portal.repository.CompanyVehicleRepository;

@Service
@RequiredArgsConstructor
public class CompanyVehicleService {
  private final CompanyVehicleRepository companyVehicleRepository;

  public void registerVehicle(NewVehicleRegisterReq req, String companyId) {

  }

  private void checkExistsVehicleByVin(String vin) {

  }

}
