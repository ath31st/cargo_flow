package ru.gnivc.logist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LogistServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(LogistServiceApplication.class, args);
  }

}
