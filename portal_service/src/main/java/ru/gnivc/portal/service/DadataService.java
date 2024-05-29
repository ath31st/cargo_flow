package ru.gnivc.portal.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.gnivc.portal.dto.company.DadataCompany;

@Service
@RequiredArgsConstructor
public class DadataService {

  @Value("${dadata.api-key}")
  private String dadataApiKey;
  @Value("${dadata.url}")
  private String dadataEndpointUrl;

  private final RestTemplate restTemplate;

  public DadataCompany getDadataCompany(String inn) {
    String jsonResponse = getCompanyDataByInn(inn);
    return convertFromJson(jsonResponse);
  }

  private String getCompanyDataByInn(String inn) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    headers.set("Accept", "application/json");
    headers.set("Authorization", "Token " + dadataApiKey);

    Map<String, String> requestBody = Map.of("query", inn);

    HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
    ResponseEntity<String> response = restTemplate.exchange(
        dadataEndpointUrl, HttpMethod.POST, entity, String.class);

    return response.getBody();
  }

  private DadataCompany convertFromJson(String json) {
    Gson gson = new Gson();
    JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
    JsonArray suggestions = jsonObject.getAsJsonArray("suggestions");
    JsonObject suggestion = suggestions.get(0).getAsJsonObject();

    JsonObject data = suggestion.getAsJsonObject("data");
    String name = data.getAsJsonObject("name").get("full_with_opf").getAsString();
    String inn = data.get("inn").getAsString();
    String kpp = data.get("kpp").getAsString();
    String ogrn = data.get("ogrn").getAsString();

    JsonObject addressObject = data.getAsJsonObject("address");
    String address = addressObject.get("value").getAsString();

    return new DadataCompany(name, inn, kpp, ogrn, address);
  }
}
