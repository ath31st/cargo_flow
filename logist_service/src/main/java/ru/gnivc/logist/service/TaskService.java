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
import ru.gnivc.logist.repository.TaskRepository;

@Service
@RequiredArgsConstructor
public class TaskService {
  private final TaskRepository taskRepository;
  private final PortalClient portalClient;


  public void createTask(int companyId, NewTaskReq newTask) {

    Task t = new Task();
    t.setStartPoint(newTask.startPoint());
    t.setEndPoint(newTask.endPoint());
    t.setDriverKeycloakId(newTask.driverKeycloakId());
    t.setCargoDescription(newTask.cargoDescription());
    t.setCompanyVehicleId(newTask.companyVehicleId());
    t.setCompanyId(companyId);

    taskRepository.save(t);
  }

  public TaskDto getTaskDto(int companyId, int taskId) {
    Task t = getTask(companyId, taskId);

    String driverFullName =
        portalClient.getCompanyDriverFullName(companyId, t.getDriverKeycloakId());
    String vehicleLicensePlate =
        portalClient.getCompanyVehicleLicensePlate(companyId, t.getCompanyVehicleId());

    return TaskDto.builder()
        .id(t.getId())
        .startPoint(t.getStartPoint())
        .endPoint(t.getEndPoint())
        .driverFullName(driverFullName)
        .cargoDescription(t.getCargoDescription())
        .companyVehicleLicensePlate(vehicleLicensePlate)
        .build();
  }

  private Task getTask(int companyId, int taskId) {
    final Optional<Task> optionalTask = taskRepository.findByCompanyIdAndId(companyId, taskId);
    return optionalTask.orElseThrow(() -> new TaskServiceException(HttpStatus.NOT_FOUND,
        "Task with id: " + taskId + " not found"));
  }
}
