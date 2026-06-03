package com.fittrack.services;

import com.fittrack.dtos.request.UsuarioRequestDTO;
import com.fittrack.dtos.response.UsuarioResponseDTO;
import com.fittrack.mappers.UsuarioMapper;
import com.fittrack.models.Suscripcion;
import com.fittrack.models.Usuario;
import com.fittrack.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Transactional
    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO requestDTO) {
        Usuario usuario = usuarioMapper.toEntity(requestDTO);
        
        // Dar suscripción PRO activa por 3 horas a todos los nuevos usuarios (Versión de prueba)
        Suscripcion suscripcion = Suscripcion.builder()
                .tipoPlan("PRO")
                .fechaInicio(new java.util.Date())
                .fechaFin(new java.util.Date(System.currentTimeMillis() + (3L * 60 * 60 * 1000))) // +3 horas
                .build();
        usuario.setSuscripcion(suscripcion);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioGuardado);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioResponseDTO> obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public boolean tieneSuscripcionActiva(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> usuario.getSuscripcion() != null && usuario.getSuscripcion().estaActiva())
                .orElse(false);
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
