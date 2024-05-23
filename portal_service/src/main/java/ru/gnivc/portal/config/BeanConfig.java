package ru.gnivc.portal.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
  @Value("${keycloak.auth-server-url}")
  private String serverUrl;
  @Value("${keycloak.auth-path}")
  private String authPath;
  @Value("${keycloak.realm}")
  private String realm;
  @Value("${keycloak.client-id}")
  private String clientId;
  @Value("${keycloak.password}")
  private String password;
  @Value("${keycloak.username}")
  private String username;
  @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
  private String issuerUri;
  @Value("${keycloak.principle-attribute}")
  private String principleAttribute;

  @Bean
  public Keycloak keycloak() {
    return KeycloakBuilder.builder()
        .serverUrl(serverUrl)
        .realm(realm)
        .grantType(OAuth2Constants.PASSWORD)
        .username(username)
        .password(password)
        .clientId(clientId)
        .build();
  }
}
