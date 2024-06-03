package ru.gnivc.logist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gnivc.logist.dto.TaskRouteDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/logist/v1/{companyId}/tasks/{taskId}/routes")
public class TaskRouteController {

  @PreAuthorize("@permissionValidator.hasCompanyLogistAccess(#companyId.toString())")
  @GetMapping("/{routeId}")
  public ResponseEntity<TaskRouteDto> getRoute(@PathVariable Integer companyId,
                                               @PathVariable Integer taskId,
                                               @PathVariable Integer routeId) {
    return ResponseEntity.ok().body(taskRouteService.getRouteDto(companyId, taskId, routeId));
  }

  @PreAuthorize("@permissionValidator.hasCompanyLogistAccess(#companyId.toString())")
  @PostMapping("/create-route")
  public ResponseEntity<HttpStatus> createRoute(
      @PathVariable Integer companyId, @PathVariable Integer taskId) {
    taskRouteService.createTaskRoute(companyId, taskId);
    return ResponseEntity.ok(HttpStatus.CREATED);
  }
}
