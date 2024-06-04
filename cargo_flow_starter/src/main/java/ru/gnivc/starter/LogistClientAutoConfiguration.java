package ru.gnivc.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.gnivc.common.client.LogistClient;
import ru.gnivc.starter.config.StarterProperties;

@Configuration
@ConditionalOnClass(LogistClient.class)
@EnableConfigurationProperties(StarterProperties.class)
public class LogistClientAutoConfiguration {

  @Bean
  public LogistClient logistClient(StarterProperties props, RestTemplate restTemplate) {
    return new LogistClient(props.getLogistUrl(), restTemplate);
  }
}
