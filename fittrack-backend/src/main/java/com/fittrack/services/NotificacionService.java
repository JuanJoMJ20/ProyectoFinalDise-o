package com.fittrack.services;

import com.fittrack.events.NuevaRutinaEvent;
import com.fittrack.events.PagoVencidoEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacionService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Async
    @EventListener
    public void manejarPagoVencido(PagoVencidoEvent event) {
        log.info("Evento recibido: Pago vencido para el usuario {}", event.getUsuario().getNombre());
        try {
            Context context = new Context();
            context.setVariable("nombre", event.getUsuario().getNombre());
            
            // Simulación de renderizado de plantilla Thymeleaf
            String process = templateEngine.process("emails/pago-vencido", context);
            
            enviarCorreo(
                "usuario@example.com", // Aquí iría event.getUsuario().getEmail() en un caso real
                "Aviso de Pago Vencido - FitTrack Pro", 
                process
            );
        } catch (Exception e) {
            log.error("Error al enviar notificación de pago vencido", e);
        }
    }

    @Async
    @EventListener
    public void manejarNuevaRutina(NuevaRutinaEvent event) {
        log.info("Evento recibido: Nueva rutina asignada al usuario {}", event.getUsuario().getNombre());
        try {
            Context context = new Context();
            context.setVariable("nombre", event.getUsuario().getNombre());
            context.setVariable("nivel", event.getPlanEntrenamiento().getNivel());
            context.setVariable("enfoque", event.getPlanEntrenamiento().getEnfoque());
            
            // Simulación de renderizado de plantilla Thymeleaf
            String process = templateEngine.process("emails/nueva-rutina", context);
            
            enviarCorreo(
                "usuario@example.com", 
                "Nueva Rutina Asignada - FitTrack Pro", 
                process
            );
        } catch (Exception e) {
            log.error("Error al enviar notificación de nueva rutina", e);
        }
    }

    private void enviarCorreo(String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true); // true indica que es HTML
        
        mailSender.send(message);
        log.info("Correo enviado a {} con asunto: {}", to, subject);
    }
}
