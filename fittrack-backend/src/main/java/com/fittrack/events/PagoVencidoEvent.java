package com.fittrack.events;

import com.fittrack.models.Usuario;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PagoVencidoEvent extends ApplicationEvent {
    
    private final Usuario usuario;
    
    public PagoVencidoEvent(Object source, Usuario usuario) {
        super(source);
        this.usuario = usuario;
    }
}
