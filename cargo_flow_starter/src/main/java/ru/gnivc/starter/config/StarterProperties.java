package ru.gnivc.starter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cargo-flow-starter")
public class StarterProperties {
  @Value("${cargo-flow-starter.portal.url}")
  private String url;

  public String getPortalUrl() {
    return url;
  }
}
