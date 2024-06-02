package ru.gnivc.logist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gnivc.logist.dto.NewTaskReq;
import ru.gnivc.logist.dto.TaskDto;
import ru.gnivc.logist.service.TaskService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/logist/v1")
public class TaskController {
  private final TaskService taskService;

  @PreAuthorize("#permissionValidator.hasCompanyLogistAccess(#companyId)")
  @GetMapping("/tasks/{companyId}/{taskId}")
  public ResponseEntity<TaskDto> getTask(@PathVariable int companyId,
                                         @PathVariable int taskId) {
    return ResponseEntity.ok().body(taskService.getTaskDto(companyId, taskId));
  }

  @PreAuthorize("#permissionValidator.hasCompanyLogistAccess(#companyId)")
  @PostMapping("/tasks/{companyId}/create-task")
  public ResponseEntity<HttpStatus> createTask(
      @PathVariable int companyId, @RequestBody NewTaskReq req) {
    taskService.createTask(companyId, req);
    return ResponseEntity.ok(HttpStatus.CREATED);
  }
}
