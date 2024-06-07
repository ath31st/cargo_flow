package ru.gnivc.logist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.gnivc.common.dto.RouteEventShortDto;
import ru.gnivc.logist.service.RouteLocationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/logist/v1/companies/{companyId}/tasks/{taskId}/routes/{routeId}")
public class RouteLocationController {
  private final RouteLocationService routeLocationService;

  @PreAuthorize("@permissionValidator.hasAccessByPermissionSet(" +
      "#companyId.toString(), @logistDriverService)")
  @GetMapping("/all-locations")
  public ResponseEntity<Page<RouteEventShortDto>> allRouteLocations(
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
