package ru.gnivc.logist.service;

import static ru.gnivc.common.event.EventType.ROUTE_CANCELLED;
import static ru.gnivc.common.event.EventType.ROUTE_ENDED;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.gnivc.common.client.PortalClient;
import ru.gnivc.common.exception.TaskServiceException;
import ru.gnivc.logist.dto.NewTaskReq;
import ru.gnivc.logist.dto.TaskDto;
import ru.gnivc.logist.entity.Task;
import ru.gnivc.logist.mapper.TaskMapper;
import ru.gnivc.logist.repository.TaskRepository;

@Service
@RequiredArgsConstructor
public class TaskService {
  private final TaskRepository taskRepository;
  private final PortalClient portalClient;
  private final TaskMapper taskMapper;

  public void createTask(Integer companyId, NewTaskReq newTask) {
    boolean validateDriverInCompany
        = portalClient.validateDriverInCompany(companyId, newTask.driverKeycloakId());
    boolean validateVehicleInCompany
        = portalClient.validateVehicleInCompany(companyId, newTask.companyVehicleId());

    if (validateDriverInCompany && validateVehicleInCompany) {
      checkAvailabilityDriverAndVehicle(
          companyId, newTask.driverKeycloakId(), newTask.companyVehicleId());

      Task task = taskMapper.toEntity(companyId, newTask);
      taskRepository.save(task);
    } else {
      throw new TaskServiceException(HttpStatus.BAD_REQUEST, "Task registration failed");
    }
  }

  public TaskDto getTaskDto(Integer companyId, Integer taskId) {
    Task t = getTask(companyId, taskId);
    return taskMapper.toDto(t, companyId);
  }

  public Task getTask(Integer companyId, Integer taskId) {
    final Optional<Task> optionalTask = taskRepository.findByCompanyIdAndId(companyId, taskId);
    return optionalTask.orElseThrow(() -> new TaskServiceException(HttpStatus.NOT_FOUND,
        "Task with id: " + taskId + " not found"));
  }

  public Page<TaskDto> getPageTask(Integer companyId, Pageable pageable) {
    return taskRepository.findAllByCompanyId(companyId, pageable)
        .map(t -> taskMapper.toDto(t, companyId));
  }

  private void checkAvailabilityDriverAndVehicle(Integer companyId,
                                                 String driverId,
                                                 Integer vehicleId) {
    if (taskRepository.checkAvailabilityDriverAndVehicle(companyId, driverId, vehicleId,
        List.of(ROUTE_ENDED.ordinal(), ROUTE_CANCELLED.ordinal()))) {
      throw new TaskServiceException(HttpStatus.CONFLICT,
          "The driver or car is already engaged in another task");
    }
  }
}
