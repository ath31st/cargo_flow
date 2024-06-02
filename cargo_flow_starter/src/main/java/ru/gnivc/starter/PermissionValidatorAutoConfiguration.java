package ru.gnivc.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.gnivc.common.validator.PermissionValidator;

@Configuration
@ConditionalOnClass(PermissionValidator.class)
public class PermissionValidatorAutoConfiguration {

  @Bean
  public PermissionValidator permissionValidator() {
    return new PermissionValidator();
  }
}
