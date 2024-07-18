package dev.canessaalvamiguel.servicecompany.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity(name = "company")
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  String name;

  @Column(name = "ruc", unique = true, nullable = false)
  String ruc;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Company company = (Company) o;
    return getId() != null && Objects.equals(getId(), company.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

}
