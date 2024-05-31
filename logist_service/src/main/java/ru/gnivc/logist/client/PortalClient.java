package ru.gnivc.logist.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PortalClient {
  private final RestTemplate restTemplate;

}
