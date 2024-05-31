package ru.gnivc.logist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "route_locations", schema = "logist_schema")
public class RouteLocation {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "route_locations_id_gen")
  @SequenceGenerator(name = "route_locations_id_gen",
      sequenceName = "route_locations_location_id_seq", allocationSize = 1)
  @Column(name = "location_id", nullable = false)
  private Integer id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "route_id", nullable = false)
  private TaskRoute route;

  @NotNull
  @Column(name = "latitude", nullable = false, precision = 9, scale = 6)
  private BigDecimal latitude;

  @NotNull
  @Column(name = "longitude", nullable = false, precision = 9, scale = 6)
  private BigDecimal longitude;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "recorded_at")
  private Instant recordedAt;
}
