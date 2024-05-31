package ru.gnivc.logist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gnivc.common.client.PortalClient;
import ru.gnivc.logist.dto.NewTaskReq;
import ru.gnivc.logist.entity.Task;
import ru.gnivc.logist.repository.TaskRepository;

@Service
@RequiredArgsConstructor
public class TaskService {
  private final TaskRepository taskRepository;
  private final PortalClient portalClient;

  public String getTask(int companyId, int taskId) {
    return "";
  }

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
}
