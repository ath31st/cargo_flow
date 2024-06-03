package ru.gnivc.portal.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.gnivc.portal.entity.CompanyVehicle;

@Repository
public interface CompanyVehicleRepository extends JpaRepository<CompanyVehicle, Integer> {
  boolean existsByVinIgnoreCaseOrLicensePlateIgnoreCase(String vin, String licensePlate);

  @Query("select cv.licensePlate "
      + "from CompanyVehicle cv "
      + "inner join cv.company c "
      + "where c.id = ?1 and cv.id = ?2")
  Optional<String> findLicensePlateByCompanyIdAndVehicleId(int companyId, int vehicleId);

  @Query("select count(cv) > 0 from CompanyVehicle cv "
      + "inner join cv.company c "
      + "where c.id = ?1 and cv.id = ?2")
  boolean existsByCompanyIdAndVehicleId(int companyId, int vehicleId);
}
