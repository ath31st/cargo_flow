package ru.gnivc.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cargo-flow-starter")
public class StarterProperties {
  private String portalUrl = "default_url";
  private String logistUrl = "default_url";

  public void setPortalUrl(String portalUrl) {
    this.portalUrl = portalUrl;
  }

  public String getPortalUrl() {
    return portalUrl;
  }

  public void setLogistUrl(String logistUrl) {
    this.logistUrl = logistUrl;
  }

  public String getLogistUrl() {
    return this.logistUrl;
  }
}
