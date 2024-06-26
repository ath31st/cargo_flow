package ru.gnivc.logist.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.gnivc.logist.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

  Optional<Task> findByCompanyIdAndId(int companyId, int taskId);

  Page<Task> findAllByCompanyId(int companyId, Pageable pageable);

  @Query("select count(t) > 0 from Task t "
      + "left join t.taskRoutes tr "
      + "where t.companyId = ?1 "
      + "and (t.driverKeycloakId = ?2 or t.companyVehicleId = ?3) "
      + "and (tr.id is null or "
      + "exists (select re from RouteEvent re "
      + "where re.route.id = tr.id "
      + "and re.id = (select max(e.id) from RouteEvent e where e.route.id = tr.id) "
      + "and re.eventType not in ?4))")
  boolean checkAvailabilityDriverAndVehicle(Integer companyId, String driverId,
                                            Integer vehicleId, List<Integer> eventTypes);

  boolean existsByIdAndDriverKeycloakId(int taskId, String driverId);

  @Query("select count(t) from Task t "
      + "where t.companyId = ?1 and t.createdAt >= CURRENT_DATE")
  int countByCompanyIdAndCreatedAtToday(int companyId);
}
