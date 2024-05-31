package ru.gnivc.logist.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.gnivc.common.validator.PermissionValidator;

@Configuration
public class BeanConfig {
  @Value("${keycloak.auth-server-url}")
  private String serverUrl;
  @Value("${keycloak.realm}")
  private String realm;
  @Value("${keycloak.client-id}")
  private String clientId;
  @Value("${keycloak.password}")
  private String password;
  @Value("${keycloak.username}")
  private String username;
  @Value("${keycloak.client-secret}")
  private String secret;

  @Bean
  public Keycloak keycloak() {
    return KeycloakBuilder.builder()
        .serverUrl(serverUrl)
        .realm(realm)
        .grantType(OAuth2Constants.PASSWORD)
        .username(username)
        .password(password)
        .clientId(clientId)
        .clientSecret(secret)
        .build();
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public PermissionValidator permissionValidator() {
    return new PermissionValidator();
  }
}