package ru.gnivc.portal.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gnivc.portal.dto.IndividualRegisterReq;
import ru.gnivc.portal.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/portal/v1")
public class Controller {
  private final UserService userService;

  @GetMapping("/roles")
  public ResponseEntity<List<String>> getAllRoles() {
    return ResponseEntity.ok().body(userService.getRealmRoles());
  }

  @PostMapping("/register-individual")
  public ResponseEntity<String> registerIndividual(@RequestBody IndividualRegisterReq req) {
    userService.checkExistsUserByEmail(req.email());
    String tempPassword = userService.registerIndividual(req);
    return new ResponseEntity<>(tempPassword, HttpStatus.CREATED);
  }
}
