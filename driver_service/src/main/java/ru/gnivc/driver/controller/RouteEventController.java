package ru.gnivc.driver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.gnivc.common.dto.RouteEventShortDto;
import ru.gnivc.driver.service.RouteEventService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/driver/v1/companies/{companyId}/tasks/{taskId}/routes/{routeId}")
public class RouteEventController {
  private final RouteEventService routeEventService;

  @PreAuthorize("@permissionValidator.hasCompanyDriverAccess(#companyId.toString())")
  @PostMapping("/create-event/{event-type}")
  public ResponseEntity<HttpStatus> createRouteEvent(@PathVariable Integer companyId,
                                                     @PathVariable Integer taskId,
                                                     @PathVariable Integer routeId,
                                                     @PathVariable("event-type") Integer eventType) {
    String driverId = SecurityContextHolder.getContext().getAuthentication().getName();
    routeEventService.createRouteEvent(companyId, taskId, routeId, eventType, driverId);
    return ResponseEntity.ok(HttpStatus.CREATED);
  }

  @PreAuthorize("@permissionValidator.hasCompanyDriverAccess(#companyId.toString())")
  @GetMapping("/all-events")
  public ResponseEntity<Page<RouteEventShortDto>> allRouteEvents(
      @RequestParam(defaultValue = "0") Integer pageNumber,
      @RequestParam(defaultValue = "10") Integer pageSize,
      @PathVariable Integer companyId,
      @PathVariable Integer taskId,
      @PathVariable Integer routeId) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    return ResponseEntity.ok().body(
        routeEventService.getPageRouteEvents(companyId, taskId, routeId, pageable));
  }
}
