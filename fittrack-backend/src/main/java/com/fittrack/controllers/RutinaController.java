package com.fittrack.controllers;

import com.fittrack.dtos.request.ProgresoRequestDTO;
import com.fittrack.dtos.request.RutinaRequestDTO;
import com.fittrack.dtos.response.UsuarioResponseDTO;
import com.fittrack.services.RutinaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class RutinaController {

    private final RutinaService rutinaService;

    @PostMapping("/{usuarioId}/rutinas")
    public ResponseEntity<UsuarioResponseDTO> asignarRutina(
            @PathVariable Long usuarioId,
            @Valid @RequestBody RutinaRequestDTO rutinaRequest) {
        
        UsuarioResponseDTO response = rutinaService.asignarPlan(usuarioId, rutinaRequest.getPlanId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{usuarioId}/progreso")
    public ResponseEntity<UsuarioResponseDTO> actualizarProgreso(
            @PathVariable Long usuarioId,
            @Valid @RequestBody ProgresoRequestDTO progresoRequest) {
        
        UsuarioResponseDTO response = rutinaService.actualizarProgreso(usuarioId, progresoRequest);
        return ResponseEntity.ok(response);
    }
}
