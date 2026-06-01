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

        PlanEntrenamiento plan = planEntrenamientoRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan de entrenamiento no encontrado con ID: " + planId));

        // Asignar la relación (El PlanEntrenamiento es el dueño de la relación en nuestro modelo)
        plan.setUsuario(usuario);
        planEntrenamientoRepository.save(plan);

        // Opcionalmente podemos agregarlo a la lista de usuario
        if (usuario.getPlanesEntrenamiento() != null) {
            usuario.getPlanesEntrenamiento().add(plan);
        }

        // Emitir evento
        eventPublisher.publishEvent(new NuevaRutinaEvent(this, usuario, plan));

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
