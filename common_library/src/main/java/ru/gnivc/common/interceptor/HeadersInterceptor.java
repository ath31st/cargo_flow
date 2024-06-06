package ru.gnivc.common.interceptor;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import ru.gnivc.common.service.ServiceNames;

@RequiredArgsConstructor
public class HeadersInterceptor implements ClientHttpRequestInterceptor {
  private final ServiceNames serviceNames;

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                      ClientHttpRequestExecution execution) throws IOException {

    request.getHeaders().set("X-Service-name", serviceNames.name());
    return execution.execute(request, body);
  }
}
