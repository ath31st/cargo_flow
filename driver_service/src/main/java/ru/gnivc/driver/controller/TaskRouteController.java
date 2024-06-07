package ru.gnivc.driver.controller;

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
import ru.gnivc.common.client.LogistClient;
import ru.gnivc.common.dto.NewTaskRouteReq;
import ru.gnivc.common.dto.TaskRouteDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/driver/v1/companies/{companyId}/tasks/{taskId}/routes")
public class TaskRouteController {
  private final LogistClient logistClient;

  @PreAuthorize("@permissionValidator.hasCompanyDriverAccess(#companyId.toString())")
  @PostMapping("/create-route")
  public ResponseEntity<HttpStatus> createRoute(@PathVariable Integer companyId,
                                                @PathVariable Integer taskId,
                                                @RequestBody NewTaskRouteReq req) {
    final HttpStatus status = logistClient.createNewTaskRouteInTask(req, companyId, taskId);
    return ResponseEntity.ok(status);
  }

  @PreAuthorize("@permissionValidator.hasCompanyDriverAccess(#companyId.toString())")
  @GetMapping("/all")
  public ResponseEntity<Page<TaskRouteDto>> allTaskRoutes(
      @RequestParam(defaultValue = "0") Integer pageNumber,
      @RequestParam(defaultValue = "10") Integer pageSize,
      @PathVariable Integer companyId,
      @PathVariable Integer taskId) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    return ResponseEntity.ok().body(logistClient.getPageTaskRoute(companyId, taskId, pageable));
  }
}
