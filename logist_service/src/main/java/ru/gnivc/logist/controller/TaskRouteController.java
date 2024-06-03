package ru.gnivc.logist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gnivc.logist.dto.TaskRouteDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/logist/v1/{companyId}/tasks/{taskId}/routes")
public class TaskRouteController {

  @PreAuthorize("@permissionValidator.hasCompanyLogistAccess(#companyId.toString())")
  @GetMapping("/{routeId}")
  public ResponseEntity<TaskRouteDto> getRoute(@PathVariable int companyId,
                                               @PathVariable int taskId,
                                               @PathVariable int routeId) {
    return ResponseEntity.ok().body(taskRouteService.getRouteDto(companyId, taskId, routeId));
  }
}
