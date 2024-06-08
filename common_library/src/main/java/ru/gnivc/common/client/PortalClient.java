package ru.gnivc.common.client;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
    final ResponseEntity<Void> response = restTemplate.getForEntity(url, Void.class);
    return response.getStatusCode().is2xxSuccessful();
  }

  public boolean validateVehicleInCompany(int companyId, int vehicleId) {
    String url = String.format("%s/companies/%d/vehicles/%s/validate",
        portalUrl, companyId, vehicleId);
    final ResponseEntity<Void> response = restTemplate.getForEntity(url, Void.class);
    return response.getStatusCode().is2xxSuccessful();
  }

  public List<Integer> getCompanyIds() {
    String url = String.format("%s/companies/all-ids", portalUrl);
    ResponseEntity<List<Integer>> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<>() {
        }
    );
    return response.getBody();
  }
}
