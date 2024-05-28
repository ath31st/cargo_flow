package ru.gnivc.portal.controller;

import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.gnivc.portal.dto.user.IndividualRegisterReq;
import ru.gnivc.portal.dto.user.NewUserDataReq;
import ru.gnivc.portal.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/portal/v1/users")
public class UserController {
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

  @PostMapping("/reset-password")
  public ResponseEntity<String> resetPassword(Principal principal, @RequestParam String email) {
    final String password = userService.changePassword(principal, email, null);
    return ResponseEntity.ok(password);
  }

  @PostMapping("/change-password")
  public ResponseEntity<HttpStatus> changePassword(
      Principal principal, @RequestParam String email, @RequestParam String newPassword) {
    userService.changePassword(principal, email, newPassword);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/change-data")
  public ResponseEntity<HttpStatus> changeUserData(
      Principal principal, @RequestBody NewUserDataReq req) {
    userService.changeData(principal, req);
    return ResponseEntity.ok().build();
  }
}
