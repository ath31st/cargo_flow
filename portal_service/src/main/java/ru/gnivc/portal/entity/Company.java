package ru.gnivc.portal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "companies")
public class Company {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "companies_id_gen")
  @SequenceGenerator(name = "companies_id_gen",
      sequenceName = "companies_company_id_seq", allocationSize = 1)
  @Column(name = "company_id", nullable = false)
  private Integer id;

  @Size(max = 200)
  @NotNull
  @Column(name = "name", nullable = false, length = 200)
  private String name;

  @Size(max = 12)
  @NotNull
  @Column(name = "inn", nullable = false, length = 12)
  private String inn;

  @Size(max = 500)
  @NotNull
  @Column(name = "address", nullable = false, length = 500)
  private String address;

  @Size(max = 9)
  @NotNull
  @Column(name = "kpp", nullable = false, length = 9)
  private String kpp;

  @Size(max = 13)
  @NotNull
  @Column(name = "ogrn", nullable = false, length = 13)
  private String ogrn;
}
