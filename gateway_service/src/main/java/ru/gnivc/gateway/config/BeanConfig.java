package ru.gnivc.gateway.config;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.web.reactive.function.client.WebClient;

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
  public JwtDecoder jwtDecoder() {
    return JwtDecoders.fromOidcIssuerLocation(issuerUri);
  }

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter
        = new JwtGrantedAuthoritiesConverter();
    converter.setPrincipalClaimName(principleAttribute);
    converter.setJwtGrantedAuthoritiesConverter(jwt -> {
      final Collection<GrantedAuthority> authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
      List<String> roles = jwt.getClaimAsStringList("spring_sec_roles");

      return Stream.concat(authorities.stream(),
              roles.stream()
                  .filter(role -> role.startsWith("ROLE_"))
                  .map(SimpleGrantedAuthority::new)
                  .map(GrantedAuthority.class::cast))
          .toList();
    });

    return converter;
  }

  @Bean
  @LoadBalanced
  public WebClient.Builder loadBalancedWebClientBuilder() {
    return WebClient.builder();
  }
}
