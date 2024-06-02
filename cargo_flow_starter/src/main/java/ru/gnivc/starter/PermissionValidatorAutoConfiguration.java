package ru.gnivc.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.gnivc.common.validator.PermissionValidator;

@Configuration
@ConfigurationPropertiesScan()
@ConditionalOnClass(PermissionValidator.class)
public class PermissionValidatorAutoConfiguration {

  @Bean
  public PermissionValidator permissionValidator() {
    return new PermissionValidator();
  }
}
