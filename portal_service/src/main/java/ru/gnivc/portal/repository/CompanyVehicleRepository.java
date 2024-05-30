package ru.gnivc.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gnivc.portal.entity.CompanyVehicle;

@Repository
public interface CompanyVehicleRepository extends JpaRepository<CompanyVehicle, Integer> {
  boolean existsByVin(String vin);
}
