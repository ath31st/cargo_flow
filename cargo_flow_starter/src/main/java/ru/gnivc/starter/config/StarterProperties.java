package ru.gnivc.starter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "cargo-flow-starter")
public class StarterProperties {
  private String portalUrl = "default_url";
  private String logistUrl = "default_url";
  private String serviceName = "default_name";
}
