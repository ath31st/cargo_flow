package ru.gnivc.common.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.gnivc.common.dto.NewTaskRouteReq;
import ru.gnivc.common.dto.RestResponsePage;
import ru.gnivc.common.dto.RouteEventShortDto;
import ru.gnivc.common.dto.TaskDto;
import ru.gnivc.common.dto.TaskRouteDto;

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

  public Page<TaskRouteDto> getPageTaskRoute(int companyId, int taskId, Pageable pageable) {
    String url = String.format("%s/companies/%d/tasks/%d/routes/all?pageNumber=%d&pageSize=%d",
        logistUrl, companyId, taskId, pageable.getPageNumber(), pageable.getPageSize());

    ResponseEntity<RestResponsePage<TaskRouteDto>> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<>() {
        }
    );

    return response.getBody();
  }

  public Page<RouteEventShortDto> getPageRouteEvents(Integer companyId, Integer taskId,
                                                     Integer routeId, Pageable pageable) {
    String url = String.format(
        "%s/companies/%d/tasks/%d/routes/%d/all-events?pageNumber=%d&pageSize=%d",
        logistUrl, companyId, taskId, routeId, pageable.getPageNumber(), pageable.getPageSize());

    ResponseEntity<RestResponsePage<RouteEventShortDto>> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<>() {
        }
    );

    return response.getBody();
  }
}
