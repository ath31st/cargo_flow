package ru.gnivc.gateway;

import java.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
  @GetMapping("/test")
  public String home(Principal principal) {
    return "Hello, " + principal.getName();
  }
}
