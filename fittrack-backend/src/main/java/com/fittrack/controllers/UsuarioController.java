package com.fittrack.controllers;

import com.fittrack.dtos.request.UsuarioRequestDTO;
import com.fittrack.dtos.response.UsuarioResponseDTO;
import com.fittrack.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerUsuarios() {
        List<UsuarioResponseDTO> usuarios = usuarioService.obtenerTodos();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(@Valid @RequestBody UsuarioRequestDTO usuarioRequest) {
        UsuarioResponseDTO nuevoUsuario = usuarioService.registrarUsuario(usuarioRequest);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/suscripcion-activa")
    public ResponseEntity<Boolean> verificarSuscripcionActiva(@PathVariable Long id) {
        boolean activa = usuarioService.tieneSuscripcionActiva(id);
        return ResponseEntity.ok(activa);
    }
}
