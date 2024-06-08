package ru.gnivc.dwh.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.gnivc.dwh.entity.CompanyStatistics;

@Repository
public interface CompanyStatisticsRepository extends MongoRepository<CompanyStatistics, Integer> {
}
