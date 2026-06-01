package com.fittrack.events;

import com.fittrack.models.PlanEntrenamiento;
import com.fittrack.models.Usuario;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NuevaRutinaEvent extends ApplicationEvent {
    
    private final Usuario usuario;
    private final PlanEntrenamiento planEntrenamiento;

    public NuevaRutinaEvent(Object source, Usuario usuario, PlanEntrenamiento planEntrenamiento) {
        super(source);
        this.usuario = usuario;
        this.planEntrenamiento = planEntrenamiento;
    }
}
