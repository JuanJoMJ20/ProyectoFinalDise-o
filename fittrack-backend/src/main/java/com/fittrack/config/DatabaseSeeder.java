package com.fittrack.config;

import com.fittrack.models.PlanEntrenamiento;
import com.fittrack.models.Usuario;
import com.fittrack.repositories.PlanEntrenamientoRepository;
import com.fittrack.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

        private final UsuarioRepository usuarioRepository;
        private final PlanEntrenamientoRepository planEntrenamientoRepository;

        @Override
        @Transactional
        public void run(String... args) throws Exception {
                log.info("Iniciando carga de datos de prueba (Seeding)...");
                
                // Limpiar plantillas anteriores
                List<PlanEntrenamiento> plantillas = planEntrenamientoRepository.findAll().stream()
                        .filter(p -> p.getUsuario() == null)
                        .toList();
                if (!plantillas.isEmpty()) {
                        planEntrenamientoRepository.deleteAll(plantillas);
                }

                // Crear los nuevos planes base (Plantillas)
                PlanEntrenamiento plan1 = PlanEntrenamiento.builder()
                                .nivel("Pérdida de peso y cardio")
                                .enfoque("Quema de grasa, resistencia cardiovascular")
                                .build();
                PlanEntrenamiento plan2 = PlanEntrenamiento.builder()
                                .nivel("Hipertrofia, fuerza y ganancia muscular")
                                .enfoque("Aumento de volumen y 5g de creatina")
                                .build();
                PlanEntrenamiento planHipertrofia = PlanEntrenamiento.builder()
                                .nivel("Personalizado")
                                .enfoque("Adaptado a las necesidades específicas del atleta")
                                .build();

                planEntrenamientoRepository.saveAll(List.of(plan1, plan2, planHipertrofia));
                log.info("Nuevos planes de entrenamiento (Plantillas) actualizados y creados.");

                if (usuarioRepository.count() == 0) {
                        Usuario usuario = Usuario.builder()
                                        .nombre("Juan Jo")
                                        .pesoActual(73.0f)
                                        .metaPeso(80.0f)
                                        .fechaNacimiento(new Date(System.currentTimeMillis() - 788400000000L))
                                        .planesEntrenamiento(new ArrayList<>())
                                        .build();

                        usuario = usuarioRepository.save(usuario);
                        log.info("Usuario de prueba 'Juan Jo' creado.");
                }

                log.info("Carga de datos finalizada.");
        }
}
