package ru.gnivc.gateway.config;

import static ru.gnivc.common.role.KeycloakRealmRoles.ADMIN;
import static ru.gnivc.common.role.KeycloakRealmRoles.LOGIST;
import static ru.gnivc.common.role.KeycloakRealmRoles.REGISTRATOR;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;
import ru.gnivc.gateway.security.CustomReactiveAuthenticationManager;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final CustomReactiveAuthenticationManager authenticationManager;

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http
        .cors(Customizer.withDefaults())
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .authorizeExchange(req -> {
          req.pathMatchers("/openid-connect/**").permitAll();

          req.pathMatchers("/portal/v1/users/roles").hasAuthority(REGISTRATOR.name());
          req.pathMatchers("portal/v1/users/register-individual").permitAll();

          req.pathMatchers("portal/v1/companies/register-company/*").hasAuthority(REGISTRATOR.name());
          req.pathMatchers("portal/v1/companies/{companyId}/register-employee").hasAnyAuthority(ADMIN.name(), LOGIST.name());
          req.pathMatchers("portal/v1/companies/{companyId}/register-vehicle").hasAnyAuthority(ADMIN.name(), LOGIST.name());
          req.pathMatchers(HttpMethod.GET, "portal/v1/companies/{companyId}").hasAuthority(ADMIN.name());
          req.pathMatchers(HttpMethod.GET, "portal/v1/companies/all").hasAuthority(ADMIN.name());
          req.anyExchange().authenticated();
        })
        .oauth2ResourceServer(oauth2 ->
            oauth2.authenticationManagerResolver(context -> Mono.just(authenticationManager)))
        .build();
  }
}
