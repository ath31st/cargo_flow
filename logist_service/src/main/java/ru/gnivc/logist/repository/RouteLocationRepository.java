package ru.gnivc.logist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.gnivc.logist.entity.RouteLocation;

@Repository
public interface RouteLocationRepository extends JpaRepository<RouteLocation, Integer> {
  @Query("select rl from RouteLocation rl "
      + "inner join rl.route r "
      + "inner join r.task t "
      + "where t.companyId = ?1 and t.id = ?2 and r.id = ?3")
  Page<RouteLocation> findPageRouteLocations(int companyId, int taskId,
                                             int routeId, Pageable pageable);
}