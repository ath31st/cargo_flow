package ru.gnivc.common.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public class PortalClient {
  private final String PORTAL_PATH = "http://portal/portal/v1/companies";
  private final RestTemplate restTemplate;

  public String getCompanyVehicleLicensePlate(int companyId, int vehicleId) {
    String url = String.format("%s/%d/vehicles/%d/license-plate", PORTAL_PATH, companyId, vehicleId);
    return restTemplate.getForObject(url, String.class);
  }

  public String getCompanyDriverFullName(int companyId, String driverId) {
    String url = String.format("%s/%d/drivers/%s/full-name", PORTAL_PATH, companyId, driverId);
    return restTemplate.getForObject(url, String.class);
  }

  public boolean validateDriverInCompany(int companyId, String driverId) {
    String url = String.format("%s/%d/drivers/%s/validate", PORTAL_PATH, companyId, driverId);
    return Boolean.TRUE.equals(restTemplate.getForObject(url, Boolean.class));
  }
}
