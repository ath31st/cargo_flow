package ru.gnivc.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.gnivc.common.client.PortalClient;
import ru.gnivc.starter.config.StarterProperties;

@Configuration
@ConditionalOnClass(PortalClient.class)
@EnableConfigurationProperties(StarterProperties.class)
public class PortalClientAutoConfiguration {

  @Bean
  public PortalClient portalClient(StarterProperties props, RestTemplate restTemplate) {
    return new PortalClient(props.getPortalUrl(), restTemplate);
  }
}
