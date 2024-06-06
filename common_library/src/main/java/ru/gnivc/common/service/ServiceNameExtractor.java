package ru.gnivc.common.service;


import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ServiceNameExtractor {

  public static Optional<ServiceNames> findServiceNameInHeaders(HttpServletRequest request) {
    Optional<ServiceNames> serviceName = Optional.empty();

    final String name = request.getHeader("X-Service-name");
    if (name != null) {
      serviceName = Optional.of(ServiceNames.valueOf(name));
    }

    return serviceName;
  }
}
