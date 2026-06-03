package com.fittrack.services;

import com.fittrack.dtos.request.ProgresoRequestDTO;
import com.fittrack.dtos.response.UsuarioResponseDTO;
import com.fittrack.events.NuevaRutinaEvent;
import com.fittrack.exceptions.ResourceNotFoundException;
import com.fittrack.mappers.UsuarioMapper;
import com.fittrack.models.PlanEntrenamiento;
import com.fittrack.models.Usuario;
import com.fittrack.repositories.PlanEntrenamientoRepository;
import com.fittrack.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RutinaService {

    private final UsuarioRepository usuarioRepository;
    private final PlanEntrenamientoRepository planEntrenamientoRepository;
    private final UsuarioMapper usuarioMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public UsuarioResponseDTO asignarPlan(Long usuarioId, Long planId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + usuarioId));

        PlanEntrenamiento planOriginal = planEntrenamientoRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan de entrenamiento no encontrado con ID: " + planId));

        // Hacemos una copia para asignarla al usuario y que no borre el template original
        PlanEntrenamiento planAsignado = PlanEntrenamiento.builder()
                .nivel(planOriginal.getNivel())
                .enfoque(planOriginal.getEnfoque())
                .usuario(usuario)
                .build();
        
        // Limpiar planes anteriores para que no se acumulen
        if (usuario.getPlanesEntrenamiento() != null && !usuario.getPlanesEntrenamiento().isEmpty()) {
            planEntrenamientoRepository.deleteAll(usuario.getPlanesEntrenamiento());
            usuario.getPlanesEntrenamiento().clear();
        }
        
        planEntrenamientoRepository.save(planAsignado);

        // Opcionalmente podemos agregarlo a la lista de usuario
        if (usuario.getPlanesEntrenamiento() != null) {
            usuario.getPlanesEntrenamiento().add(planAsignado);
        }

        // Emitir evento
        eventPublisher.publishEvent(new NuevaRutinaEvent(this, usuario, planAsignado));

        return usuarioMapper.toResponseDTO(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponseDTO actualizarProgreso(Long usuarioId, ProgresoRequestDTO progresoDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + usuarioId));

        usuario.setPesoActual(progresoDTO.getPesoActual());
        
        return usuarioMapper.toResponseDTO(usuarioRepository.save(usuario));
    }
}
