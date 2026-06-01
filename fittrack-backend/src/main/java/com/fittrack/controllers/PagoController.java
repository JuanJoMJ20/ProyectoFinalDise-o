package com.fittrack.controllers;

import com.fittrack.dtos.request.PagoRequestDTO;
import com.fittrack.services.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @PostMapping("/procesar")
    public ResponseEntity<Map<String, String>> procesarPago(@Valid @RequestBody PagoRequestDTO pagoRequest) {
        String transaccionId = pagoService.procesarPago(pagoRequest);
        
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Pago procesado exitosamente");
        response.put("transaccionId", transaccionId);
        
        return ResponseEntity.ok(response);
    }
}
