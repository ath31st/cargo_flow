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
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "route_events", schema = "logist_schema")
public class RouteEvent {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "route_events_id_gen")
  @SequenceGenerator(name = "route_events_id_gen",
      sequenceName = "route_events_event_id_seq", allocationSize = 1)
  @Column(name = "event_id", nullable = false)
  private Integer id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "route_id", nullable = false)
  private TaskRoute route;

  @NotNull
  @Column(name = "event_type", nullable = false)
  private Integer eventType;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "event_time")
  private Instant eventTime;
}
