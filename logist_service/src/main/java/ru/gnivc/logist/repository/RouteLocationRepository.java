package ru.gnivc.logist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gnivc.logist.entity.RouteLocation;

@Repository
public interface RouteLocationRepository extends JpaRepository<RouteLocation, Integer> {
}