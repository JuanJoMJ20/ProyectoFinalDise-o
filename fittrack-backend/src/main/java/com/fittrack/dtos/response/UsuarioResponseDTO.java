package com.fittrack.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {

    private Long id;
    private String nombre;
    private float pesoActual;
    private float metaPeso;
    private boolean suscripcionActiva;
    private String rutinaActual;

}
