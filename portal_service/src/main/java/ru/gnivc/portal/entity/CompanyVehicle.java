package ru.gnivc.portal.entity;

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
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "company_vehicles")
public class CompanyVehicle {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_vehicles_id_gen")
  @SequenceGenerator(name = "company_vehicles_id_gen",
      sequenceName = "company_vehicles_company_vehicle_id_seq", allocationSize = 1)
  @Column(name = "company_vehicle_id", nullable = false)
  private Integer id;

  @Size(max = 17)
  @NotNull
  @Column(name = "vin", nullable = false, length = 17)
  private String vin;

  @NotNull
  @Column(name = "year", nullable = false)
  private Integer year;

  @Size(max = 20)
  @NotNull
  @Column(name = "license_plate", nullable = false)
  private String licensePlate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "company_id")
  private Company company;
}
