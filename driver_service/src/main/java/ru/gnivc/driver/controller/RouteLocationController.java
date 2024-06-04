package ru.gnivc.driver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gnivc.driver.dto.NewRouteLocationReq;
import ru.gnivc.driver.service.RouteLocationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/driver/v1/companies/{companyId}/tasks/{taskId}/routes/{routeId}")
public class RouteLocationController {
  private final RouteLocationService routeLocationService;

  @PreAuthorize("@permissionValidator.hasCompanyDriverAccess(#companyId.toString())")
  @PostMapping("/create-location")
  public ResponseEntity<HttpStatus> createRouteLocation(@PathVariable Integer companyId,
                                                        @PathVariable Integer taskId,
                                                        @PathVariable Integer routeId,
                                                        @RequestBody NewRouteLocationReq req) {
    String driverId = SecurityContextHolder.getContext().getAuthentication().getName();
    routeLocationService.createRouteLocation(companyId, taskId, routeId, driverId, req);
    return ResponseEntity.ok(HttpStatus.CREATED);
  }
}
