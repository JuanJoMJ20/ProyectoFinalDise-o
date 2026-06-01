package com.fittrack.services;

import com.fittrack.events.PagoVencidoEvent;
import com.fittrack.models.Suscripcion;
import com.fittrack.repositories.SuscripcionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SuscripcionScheduler {

    private final SuscripcionRepository suscripcionRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Cron que se ejecuta todos los días a la medianoche (00:00:00)
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void verificarSuscripcionesVencidas() {
        log.info("Iniciando cron job: Verificación de suscripciones vencidas...");

        // Obtenemos el día de ayer
        LocalDate ayerLocal = LocalDate.now().minusDays(1);
        Date ayer = Date.from(ayerLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Suscripcion> suscripcionesVencidas = suscripcionRepository.findByFechaFin(ayer);

        log.info("Se encontraron {} suscripciones que vencieron ayer.", suscripcionesVencidas.size());

        for (Suscripcion suscripcion : suscripcionesVencidas) {
            if (suscripcion.getUsuario() != null) {
                log.info("Emitiendo evento de Pago Vencido para el usuario ID: {}", suscripcion.getUsuario().getId());
                eventPublisher.publishEvent(new PagoVencidoEvent(this, suscripcion.getUsuario()));
            }
        }
    }
}
