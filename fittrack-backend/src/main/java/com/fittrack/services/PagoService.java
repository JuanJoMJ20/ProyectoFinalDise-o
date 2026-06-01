package com.fittrack.services;

import com.fittrack.dtos.request.PagoRequestDTO;
import com.fittrack.exceptions.ResourceNotFoundException;
import com.fittrack.models.Suscripcion;
import com.fittrack.models.Transaccion;
import com.fittrack.models.Usuario;
import com.fittrack.repositories.SuscripcionRepository;
import com.fittrack.repositories.TransaccionRepository;
import com.fittrack.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class PagoService {

    private final UsuarioRepository usuarioRepository;
    private final SuscripcionRepository suscripcionRepository;
    private final TransaccionRepository transaccionRepository;

    @Transactional
    public String procesarPago(PagoRequestDTO requestDTO) {
        log.info("Iniciando procesamiento de pago vía {} para el usuario {}", 
                requestDTO.getMetodoPago(), requestDTO.getUsuarioId());

        Usuario usuario = usuarioRepository.findById(requestDTO.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + requestDTO.getUsuarioId()));

        // Simulación de pasarela de pagos (Nequi, Nu Bank, Stripe, etc.)
        boolean pagoExitoso = simularPasarela(requestDTO.getMetodoPago());
        
        if (!pagoExitoso) {
            throw new RuntimeException("El pago fue rechazado por la pasarela: " + requestDTO.getMetodoPago());
        }

        Suscripcion suscripcion = usuario.getSuscripcion();
        if (suscripcion == null) {
            suscripcion = Suscripcion.builder()
                    .tipoPlan("Premium Mensual")
                    .fechaInicio(new Date())
                    .build();
        }

        // Extender la suscripción por 30 días
        Date fechaBase = (suscripcion.estaActiva()) ? suscripcion.getFechaFin() : new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaBase);
        calendar.add(Calendar.DAY_OF_YEAR, 30);
        suscripcion.setFechaFin(calendar.getTime());

        // Es importante persistir la suscripción primero si es nueva
        suscripcion = suscripcionRepository.save(suscripcion);
        
        if (usuario.getSuscripcion() == null) {
            usuario.setSuscripcion(suscripcion);
            usuarioRepository.save(usuario);
        }

        // Registrar la transacción
        Transaccion transaccion = Transaccion.builder()
                .monto(requestDTO.getMonto())
                .fecha(new Date())
                .estado("APROBADO")
                .suscripcion(suscripcion)
                .build();
                
        transaccion = transaccionRepository.save(transaccion);

        log.info("Pago exitoso. Transacción ID: {}", transaccion.getIdTransaccion());
        
        return transaccion.getIdTransaccion();
    }

    private boolean simularPasarela(String metodoPago) {
        // En un entorno real, aquí haríamos peticiones HTTP a Stripe, Nequi, etc.
        log.info("Conectando con la API de {}...", metodoPago);
        try {
            Thread.sleep(500); // Simulando latencia de red
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return true; // Asumimos que siempre es exitoso en esta simulación
    }
}
