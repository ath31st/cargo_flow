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
import ru.gnivc.common.dto.TaskDto;
import ru.gnivc.logist.dto.NewTaskReq;
import ru.gnivc.logist.service.TaskService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/logist/v1/companies/{companyId}/tasks")
public class TaskController {
  private final TaskService taskService;

  @PreAuthorize("@permissionValidator.hasAccessByPermissionSet(" +
      "#companyId.toString(), @logistOrDriverService)")
  @GetMapping("/{taskId}")
  public ResponseEntity<TaskDto> getTask(@PathVariable Integer companyId,
                                         @PathVariable Integer taskId,
                                         HttpServletRequest request) {
    taskService.checkRequestFromDriverService(taskId, request);
    return ResponseEntity.ok().body(taskService.getTaskDto(companyId, taskId));
  }

  @PreAuthorize("@permissionValidator.hasCompanyLogistAccess(#companyId.toString())")
  @PostMapping("/create-task")
  public ResponseEntity<HttpStatus> createTask(
      @PathVariable Integer companyId, @RequestBody NewTaskReq req) {
    taskService.createTask(companyId, req);
    return ResponseEntity.ok(HttpStatus.CREATED);
  }

  @PreAuthorize("@permissionValidator.hasCompanyLogistAccess(#companyId.toString())")
  @GetMapping("/all")
  public ResponseEntity<Page<TaskDto>> allCompanyTask(
      @RequestParam(defaultValue = "0") Integer pageNumber,
      @RequestParam(defaultValue = "10") Integer pageSize,
      @PathVariable Integer companyId) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    return ResponseEntity.ok().body(taskService.getPageTask(companyId, pageable));
  }
}
