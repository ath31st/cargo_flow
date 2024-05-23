package ru.gnivc.portal.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gnivc.portal.service.KeycloakService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/portal/v1")
public class Controller {
  private final KeycloakService keycloakService;

  @GetMapping("/roles")
  public ResponseEntity<List<String>> getAllRoles() {
    return ResponseEntity.ok().body(keycloakService.getRealmRoles());
  }
}
