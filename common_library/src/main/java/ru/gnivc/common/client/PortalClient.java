package ru.gnivc.common.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public class PortalClient {
  private final String PORTAL_PATH = "http://portal/portal/v1/companies";
  private final RestTemplate restTemplate;

  public String getCompanyVehicleLicensePlate(int companyId, int vehicleId) {
    String url = String.format("%s/%d/vehicles/%d", PORTAL_PATH, companyId, vehicleId);
    return restTemplate.getForObject(url, String.class);
  }
}
