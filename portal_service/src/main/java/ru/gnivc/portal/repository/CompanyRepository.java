package ru.gnivc.portal.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.gnivc.portal.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
  boolean existsByInn(String inn);

  @Query("select c.id from Company c")
  List<Integer> findAllCompanyIds();
}
