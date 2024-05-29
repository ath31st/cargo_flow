package ru.gnivc.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gnivc.portal.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
  boolean existsByInn(String inn);
}
