package ru.gnivc.logist.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.gnivc.logist.entity.TaskRoute;

@Repository
public interface TaskRouteRepository extends JpaRepository<TaskRoute, Integer> {
  @Query("select tr from TaskRoute tr "
      + "inner join Task t "
      + "where t.companyId = ?1 and t.id = ?2 and tr.id = ?3")
  Optional<TaskRoute> findTaskRoute(int companyId, int taskId, int routeId);
}