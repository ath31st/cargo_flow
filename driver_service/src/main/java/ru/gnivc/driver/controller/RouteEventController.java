package ru.gnivc.driver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gnivc.common.dto.RouteEventDto;
import ru.gnivc.driver.service.RouteEventProducer;

@RestController
@RequiredArgsConstructor
@RequestMapping("/driver/v1/companies/{companyId}/tasks/{taskId}/routes/{routeId}")
public class RouteEventController {
  private final RouteEventProducer routeEventProducer;

  @PreAuthorize("@permissionValidator.hasCompanyDriverAccess(#companyId.toString())")
  @PostMapping("/create-event/{event-type}")
  public ResponseEntity<HttpStatus> createRoute(@PathVariable Integer companyId,
                                                @PathVariable Integer taskId,
                                                @PathVariable Integer routeId,
                                                @PathVariable("event-type") Integer eventType) {
    RouteEventDto dto = new RouteEventDto(companyId, taskId, routeId, eventType);

    return ResponseEntity.ok(HttpStatus.CREATED);
  }

//  @PreAuthorize("@permissionValidator.hasCompanyLogistAccess(#companyId.toString())")
//  @GetMapping("/all")
//  public ResponseEntity<Page<TaskRouteDto>> allTaskRoutes(
//      @RequestParam(defaultValue = "0") Integer pageNumber,
//      @RequestParam(defaultValue = "10") Integer pageSize,
//      @PathVariable Integer companyId,
//      @PathVariable Integer taskId) {
//    Pageable pageable = PageRequest.of(pageNumber, pageSize);
//    return ResponseEntity.ok().body(taskRouteService.getPageTaskRoute(companyId, taskId, pageable));
//  }
}