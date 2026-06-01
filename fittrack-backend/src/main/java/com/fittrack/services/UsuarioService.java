package com.fittrack.services;

import com.fittrack.dtos.request.UsuarioRequestDTO;
import com.fittrack.dtos.response.UsuarioResponseDTO;
import com.fittrack.mappers.UsuarioMapper;
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
}
