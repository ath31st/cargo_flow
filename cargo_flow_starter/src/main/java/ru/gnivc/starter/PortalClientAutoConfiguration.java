package ru.gnivc.starter;

import java.util.Collections;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.gnivc.common.client.PortalClient;
import ru.gnivc.common.interceptor.TokenInterceptor;
import ru.gnivc.starter.config.StarterProperties;

@Configuration
@ConditionalOnClass(PortalClient.class)
@EnableConfigurationProperties(StarterProperties.class)
public class PortalClientAutoConfiguration {

  @Bean
  @LoadBalanced
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setInterceptors(Collections.singletonList(new TokenInterceptor()));
    return restTemplate;
  }

  @Bean
  public PortalClient portalClient(StarterProperties props, RestTemplate restTemplate) {
    return new PortalClient(props.getPortalUrl(), restTemplate);
  }
}
