package ru.gnivc.starter;

import java.util.Collections;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.gnivc.common.client.LogistClient;
import ru.gnivc.common.interceptor.TokenInterceptor;
import ru.gnivc.starter.config.StarterProperties;

@Configuration
@ConditionalOnClass(RestTemplate.class)
@EnableConfigurationProperties(StarterProperties.class)
public class RestTemplateAutoConfiguration {

  @Bean
  @LoadBalanced
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setInterceptors(Collections.singletonList(new TokenInterceptor()));
    return restTemplate;
  }
}
