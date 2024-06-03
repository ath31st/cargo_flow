package ru.gnivc.logist.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
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

  public void createTask(int companyId, NewTaskReq newTask) {
    boolean validateDriverInCompany
        = portalClient.validateDriverInCompany(companyId, newTask.driverKeycloakId());
    boolean validateVehicleInCompany
        = portalClient.validateVehicleInCompany(companyId, newTask.companyVehicleId());

    if (validateDriverInCompany && validateVehicleInCompany) {
      Task task = taskMapper.toEntity(companyId, newTask);
      taskRepository.save(task);
    } else {
      throw new TaskServiceException(HttpStatus.BAD_REQUEST, "Task registration failed");
    }
  }

  public TaskDto getTaskDto(int companyId, int taskId) {
    Task t = getTask(companyId, taskId);
    return taskMapper.toDto(t, companyId);
  }

  private Task getTask(int companyId, int taskId) {
    final Optional<Task> optionalTask = taskRepository.findByCompanyIdAndId(companyId, taskId);
    return optionalTask.orElseThrow(() -> new TaskServiceException(HttpStatus.NOT_FOUND,
        "Task with id: " + taskId + " not found"));
  }
}
