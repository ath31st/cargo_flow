package ru.gnivc.driver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gnivc.common.client.LogistClient;
import ru.gnivc.common.dto.TaskDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/driver/v1/companies/{companyId}/tasks")
public class TaskController {
  private final LogistClient logistClient;

  @PreAuthorize("@permissionValidator.hasCompanyDriverAccess(#companyId.toString())")
  @GetMapping("/{taskId}")
  public ResponseEntity<TaskDto> getTask(@PathVariable Integer companyId,
                                         @PathVariable Integer taskId) {
    return ResponseEntity.ok().body(logistClient.getTaskDtoById(companyId, taskId));
  }

//  @PreAuthorize("@permissionValidator.hasCompanyLogistAccess(#companyId.toString())")
//  @GetMapping("/all")
//  public ResponseEntity<Page<TaskDto>> allCompanyTask(
//      @RequestParam(defaultValue = "0") Integer pageNumber,
//      @RequestParam(defaultValue = "10") Integer pageSize,
//      @PathVariable Integer companyId) {
//    Pageable pageable = PageRequest.of(pageNumber, pageSize);
//    return ResponseEntity.ok().body(taskService.getPageTask(companyId, pageable));
//  }
}
