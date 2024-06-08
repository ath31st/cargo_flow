package ru.gnivc.logist.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.gnivc.logist.entity.TaskRoute;

@Repository
public interface TaskRouteRepository extends JpaRepository<TaskRoute, Integer> {
  @Query("select tr from TaskRoute tr "
      + "inner join tr.task t "
      + "where t.companyId = ?1 and t.id = ?2 and tr.id = ?3")
  Optional<TaskRoute> findTaskRoute(int companyId, int taskId, int routeId);

  @Query("select tr from TaskRoute tr "
      + "inner join tr.task t "
      + "where t.companyId = ?1 and t.id = ?2")
  Page<TaskRoute> findAllByCompanyIdAndTaskId(int companyId, int taskId, Pageable pageable);

  @Query("select count(tr) > 0 from TaskRoute tr "
      + "inner join tr.task t "
      + "where t.companyId = ?1 and t.id = ?2 and t.driverKeycloakId = ?3 and tr.id = ?4")
  boolean existsByCompanyIdAndTaskIdAndDriverIdAndId(int companyId, int taskId,
                                                     String driverId, int taskRouteId);

  @Query("select count(tr) from TaskRoute tr "
      + "inner join tr.task t "
      + "inner join tr.routeEvents re "
      + "where t.companyId = ?1 "
      + "and tr.createdAt >= CURRENT_DATE "
      + "and re.id = (select max(e.id) from RouteEvent e where e.route.id = tr.id) "
      + "and re.eventType = ?2")
  int countByCompanyIdAndLastEventType(int companyId, int lastEventType);
}