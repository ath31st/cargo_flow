package ru.gnivc.dwh.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "statistics_collection")
public class CompanyStatistics {

  @Id
  private String companyId;
  private String rawStatistics;
}
