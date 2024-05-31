package ru.gnivc.logist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "tasks", schema = "logist_schema")
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tasks_id_gen")
  @SequenceGenerator(name = "tasks_id_gen", sequenceName = "tasks_task_id_seq", allocationSize = 1)
  @Column(name = "task_id", nullable = false)
  private Integer id;

  @Size(max = 255)
  @NotNull
  @Column(name = "start_point", nullable = false)
  private String startPoint;

  @Size(max = 255)
  @NotNull
  @Column(name = "end_point", nullable = false)
  private String endPoint;

  @Size(max = 40)
  @NotNull
  @Column(name = "driver_keycloak_id", nullable = false, length = 40)
  private String driverKeycloakId;

  @NotNull
  @Column(name = "cargo_description", nullable = false, length = Integer.MAX_VALUE)
  private String cargoDescription;

  @NotNull
  @Column(name = "company_vehicle_id", nullable = false)
  private Integer companyVehicleId;

  @NotNull
  @Column(name = "company_id", nullable = false)
  private Integer companyId;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "created_at")
  private Instant createdAt;

  @OneToMany(mappedBy = "task")
  private Set<TaskRoute> taskRoutes = new LinkedHashSet<>();
}
