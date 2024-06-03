package ru.gnivc.logist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gnivc.logist.entity.RouteEvent;

@Repository
public interface RouteEventRepository extends JpaRepository<RouteEvent, Integer> {
}