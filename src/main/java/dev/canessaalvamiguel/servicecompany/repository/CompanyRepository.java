package dev.canessaalvamiguel.servicecompany.repository;

import dev.canessaalvamiguel.servicecompany.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
  Company findByRuc(String ruc);
}
