package dev.canessaalvamiguel.serviceuser.entities;

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
@Entity(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  String name;
  String lastname;

  @Column(name = "email", unique = true, nullable = false)
  String email;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    User contract = (User) o;
    return getId() != null && Objects.equals(getId(), contract.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

}
