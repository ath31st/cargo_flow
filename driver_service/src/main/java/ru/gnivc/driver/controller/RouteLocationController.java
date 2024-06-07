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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.gnivc.common.dto.RouteLocationShortDto;
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

  @PreAuthorize("@permissionValidator.hasCompanyDriverAccess(#companyId.toString())")
  @GetMapping("/all-locations")
  public ResponseEntity<Page<RouteLocationShortDto>> allRouteLocations(
      @RequestParam(defaultValue = "0") Integer pageNumber,
      @RequestParam(defaultValue = "10") Integer pageSize,
      @PathVariable Integer companyId,
      @PathVariable Integer taskId,
      @PathVariable Integer routeId) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    return ResponseEntity.ok().body(
        routeLocationService.getPageRouteLocations(companyId, taskId, routeId, pageable));
  }
}
