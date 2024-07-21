package dev.canessaalvamiguel.servicecompany.repository;

import dev.canessaalvamiguel.servicecompany.entities.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository  extends JpaRepository<Area, Long> {
}
