package ru.gnivc.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/test"))
        .build();
  }
//    return http
//        .httpBasic(Customizer.withDefaults())
//        .cors(Customizer.withDefaults())
//        .csrf(ServerHttpSecurity.CsrfSpec::disable)
//        .authorizeExchange(req -> {
//          req.pathMatchers("/test").permitAll();
//          req.anyExchange().authenticated();
//        })
//        .oauth2Login(Customizer.withDefaults())
//        .oauth2ResourceServer(Customizer.withDefaults())
//        .build();
//  }
}
