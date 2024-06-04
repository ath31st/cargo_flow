package ru.gnivc.logist.mapper;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gnivc.common.client.PortalClient;
import ru.gnivc.logist.dto.NewTaskReq;
import ru.gnivc.common.dto.TaskDto;
import ru.gnivc.logist.entity.Task;

@Component
@RequiredArgsConstructor
public class TaskMapper {

  private final PortalClient portalClient;

  public Task toEntity(int companyId, NewTaskReq newTask) {
    Task t = new Task();
    t.setStartPoint(newTask.startPoint());
    t.setEndPoint(newTask.endPoint());
    t.setDriverKeycloakId(newTask.driverKeycloakId());
    t.setCargoDescription(newTask.cargoDescription());
    t.setCompanyVehicleId(newTask.companyVehicleId());
    t.setCompanyId(companyId);
    t.setCreatedAt(Instant.now());
    return t;
  }

  public TaskDto toDto(Task task, int companyId) {
    String driverFullName =
        portalClient.getCompanyDriverFullName(companyId, task.getDriverKeycloakId());
    String vehicleLicensePlate =
        portalClient.getCompanyVehicleLicensePlate(companyId, task.getCompanyVehicleId());

    return TaskDto.builder()
        .id(task.getId())
        .startPoint(task.getStartPoint())
        .endPoint(task.getEndPoint())
        .driverFullName(driverFullName)
        .cargoDescription(task.getCargoDescription())
        .companyVehicleLicensePlate(vehicleLicensePlate)
        .build();
  }
}
