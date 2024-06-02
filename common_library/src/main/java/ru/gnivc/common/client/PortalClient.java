package ru.gnivc.common.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public class PortalClient {
  private final String portalUrl;
  private final RestTemplate restTemplate;

  public String getCompanyVehicleLicensePlate(int companyId, int vehicleId) {
    String url = String.format("%s/companies/%d/vehicles/%d/license-plate",
        portalUrl, companyId, vehicleId);
    return restTemplate.getForObject(url, String.class);
  }

  public String getCompanyDriverFullName(int companyId, String driverId) {
    String url = String.format("%s/companies/%d/drivers/%s/full-name",
        portalUrl, companyId, driverId);
    return restTemplate.getForObject(url, String.class);
  }

  public boolean validateDriverInCompany(int companyId, String driverId) {
    String url = String.format("%s/companies/%d/drivers/%s/validate",
        portalUrl, companyId, driverId);
    return Boolean.TRUE.equals(restTemplate.getForObject(url, Boolean.class));
  }
}
