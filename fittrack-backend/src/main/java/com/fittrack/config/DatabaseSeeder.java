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

                if (planEntrenamientoRepository.count() == 0 && usuarioRepository.count() == 0) {
                        PlanEntrenamiento plan1 = PlanEntrenamiento.builder()
                                        .nivel("Principiante")
                                        .enfoque("Pérdida de peso y cardio")
                                        .build();
                        PlanEntrenamiento plan2 = PlanEntrenamiento.builder()
                                        .nivel("Avanzado")
                                        .enfoque("Hipertrofia y fuerza")
                                        .build();
                        PlanEntrenamiento planHipertrofia = PlanEntrenamiento.builder()
                                        .nivel("Intermedio/Avanzado")
                                        .enfoque("Ganancia de volumen, masa muscular y dosificación de 5g de creatina")
                                        .build();

                        planEntrenamientoRepository.saveAll(List.of(plan1, plan2, planHipertrofia));
                        log.info("Planes de entrenamiento creados.");

                        Usuario usuario = Usuario.builder()
                                        .nombre("Juan Jo")
                                        .pesoActual(73.0f)
                                        .metaPeso(80.0f)
                                        // Fecha de nacimiento aproximada (Ej. hace 25 años)
                                        .fechaNacimiento(new Date(System.currentTimeMillis() - 788400000000L))
                                        .planesEntrenamiento(new ArrayList<>())
                                        .build();

                        usuario = usuarioRepository.save(usuario);
                        log.info("Usuario de prueba 'Juan Jo' creado.");

                        // Asignar el Plan Hipertrofia al usuario
                        planHipertrofia.setUsuario(usuario);
                        planEntrenamientoRepository.save(planHipertrofia);
                        usuario.getPlanesEntrenamiento().add(planHipertrofia);
                        usuarioRepository.save(usuario);

                        log.info("Plan Hipertrofia asignado a Juan Jo.");
                }

                log.info("Carga de datos finalizada.");
        }
}
