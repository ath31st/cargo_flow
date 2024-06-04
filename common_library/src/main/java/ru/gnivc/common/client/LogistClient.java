package ru.gnivc.common.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.gnivc.common.dto.NewTaskRouteReq;
import ru.gnivc.common.dto.TaskDto;

@RequiredArgsConstructor
public class LogistClient {
  private final String logistUrl;
  private final RestTemplate restTemplate;

  public TaskDto getTaskDtoById(int companyId, int taskId) {
    String url = String.format("%s/companies/%d/tasks/%d", logistUrl, companyId, taskId);
    return restTemplate.getForObject(url, TaskDto.class);
  }

  public HttpStatus createNewTaskRouteInTask(NewTaskRouteReq req, int companyId, int taskId) {
    String url = String.format("%s/companies/%d/tasks/%d/routes/create-route",
        logistUrl, companyId, taskId);
    return restTemplate.postForObject(url, req, HttpStatus.class);
  }
}
