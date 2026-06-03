package com.fittrack.controllers;

import com.fittrack.models.PlanEntrenamiento;
import com.fittrack.repositories.PlanEntrenamientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/planes")
@RequiredArgsConstructor
public class PlanEntrenamientoController {

    private final PlanEntrenamientoRepository planEntrenamientoRepository;

    @GetMapping
    public ResponseEntity<List<PlanEntrenamiento>> obtenerPlanes() {
        return ResponseEntity.ok(planEntrenamientoRepository.findAll());
    }
}
