package ru.gnivc.logist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gnivc.logist.repository.RouteEventRepository;

@Service
@RequiredArgsConstructor
public class RouteEventService {
  private final RouteEventRepository routeEventRepository;

}
