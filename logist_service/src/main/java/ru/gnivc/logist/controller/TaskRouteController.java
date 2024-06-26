package ru.gnivc.logist.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.gnivc.common.dto.NewTaskRouteReq;
import ru.gnivc.common.dto.TaskRouteDto;
import ru.gnivc.logist.service.TaskRouteService;
import ru.gnivc.logist.service.TaskService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/logist/v1/companies/{companyId}/tasks/{taskId}/routes")
public class TaskRouteController {
  private final TaskRouteService taskRouteService;
  private final TaskService taskService;

  @PreAuthorize("@permissionValidator.hasCompanyLogistAccess(#companyId.toString())")
  @GetMapping("/{routeId}")
  public ResponseEntity<TaskRouteDto> getRoute(@PathVariable Integer companyId,
                                               @PathVariable Integer taskId,
                                               @PathVariable Integer routeId) {
    return ResponseEntity.ok().body(taskRouteService.getRouteDto(companyId, taskId, routeId));
  }

  @PreAuthorize("@permissionValidator.hasAccessByPermissionSet(" +
      "#companyId.toString(), @logistOrDriverService)")
  @PostMapping("/create-route")
  public ResponseEntity<HttpStatus> createRoute(@PathVariable Integer companyId,
                                                @PathVariable Integer taskId,
                                                @RequestBody NewTaskRouteReq req,
                                                HttpServletRequest request) {
    taskService.checkRequestFromDriverService(taskId, request);
    taskRouteService.createTaskRoute(companyId, taskId, req);
    return ResponseEntity.ok(HttpStatus.CREATED);
  }

  @PreAuthorize("@permissionValidator.hasAccessByPermissionSet(" +
      "#companyId.toString(), @logistOrDriverService)")
  @GetMapping("/all")
  public ResponseEntity<Page<TaskRouteDto>> allTaskRoutes(
      @RequestParam(defaultValue = "0") Integer pageNumber,
      @RequestParam(defaultValue = "10") Integer pageSize,
      @PathVariable Integer companyId,
      @PathVariable Integer taskId,
      HttpServletRequest request) {
    taskService.checkRequestFromDriverService(taskId, request);
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    return ResponseEntity.ok().body(taskRouteService.getPageTaskRoute(companyId, taskId, pageable));
  }
}
