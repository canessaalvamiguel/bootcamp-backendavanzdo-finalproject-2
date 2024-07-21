package dev.canessaalvamiguel.servicecompany.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Entity(name = "employees")
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String lastname;

  @ManyToOne
  @JoinColumn(name = "company_id", nullable = false)
  @JsonBackReference
  private Company company;

  @ManyToOne
  @JoinColumn(name = "area_id", nullable = false)
  private Area area;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Employee employee = (Employee) o;
    return getId() != null && Objects.equals(getId(), employee.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
