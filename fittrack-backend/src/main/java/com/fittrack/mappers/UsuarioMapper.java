package com.fittrack.mappers;

import com.fittrack.dtos.request.UsuarioRequestDTO;
import com.fittrack.dtos.response.UsuarioResponseDTO;
import com.fittrack.models.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    /**
     * Convierte de UsuarioRequestDTO a Entidad Usuario.
     */
    public Usuario toEntity(UsuarioRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        return Usuario.builder()
                .nombre(requestDTO.getNombre())
                .pesoActual(requestDTO.getPesoActual())
                .metaPeso(requestDTO.getMetaPeso())
                .fechaNacimiento(requestDTO.getFechaNacimiento())
                .build();
    }

    /**
     * Convierte de Entidad Usuario a UsuarioResponseDTO.
     */
    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        boolean suscripcionActiva = usuario.getSuscripcion() != null && usuario.getSuscripcion().estaActiva();

        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .pesoActual(usuario.getPesoActual())
                .metaPeso(usuario.getMetaPeso())
                .suscripcionActiva(suscripcionActiva)
                .build();
    }
}
