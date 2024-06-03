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
}