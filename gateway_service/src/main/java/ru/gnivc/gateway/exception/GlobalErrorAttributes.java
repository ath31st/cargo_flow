package ru.gnivc.gateway.exception;

import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

  @Override
  public Map<String, Object> getErrorAttributes(ServerRequest request,
                                                ErrorAttributeOptions options) {
    Map<String, Object> map = super.getErrorAttributes(request, options);
    Throwable error = getError(request);
    String errorMessage = error.getMessage();

    map.put("status", HttpStatus.BAD_REQUEST.value());
    map.put("message", errorMessage);
    return map;
  }
}
