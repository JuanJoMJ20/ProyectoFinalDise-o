package com.fittrack.repositories;

import com.fittrack.models.PlanEntrenamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanEntrenamientoRepository extends JpaRepository<PlanEntrenamiento, Long> {
}
