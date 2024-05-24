package ru.gnivc.gateway.security;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {
  @Value("${keycloak.realm}")
  private String realm;
  private final ReactiveJwtDecoder jwtDecoder;
  private final Keycloak keycloak;

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    BearerTokenAuthenticationToken token = (BearerTokenAuthenticationToken) authentication;
    return jwtDecoder.decode(token.getToken())
        .flatMap(jwt -> {
          String userId = jwt.getSubject();

          UserResource userResource = keycloak.realm(realm).users().get(userId);
          final List<RoleRepresentation> roleRepresentations = userResource.roles()
              .realmLevel()
              .listAll();

          List<GrantedAuthority> authorities = roleRepresentations.stream()
              .map(rr -> (GrantedAuthority) new SimpleGrantedAuthority(rr.getName()))
              .toList();

          UserRepresentation userRepresentation = userResource.toRepresentation();
          CustomUserDetails userDetails = new CustomUserDetails(
              userRepresentation.getUsername(), "", authorities);

          return Mono.just((Authentication) new JwtAuthenticationToken(
              jwt, authorities, userDetails.getUsername()));
        });
  }
}
