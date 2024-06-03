package ru.gnivc.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cargo-flow-starter")
public class StarterProperties {
  private String portalUrl;

  public void setPortalUrl(String portalUrl) {
    this.portalUrl = portalUrl;
  }

  public String getPortalUrl() {
    return portalUrl;
  }
}
