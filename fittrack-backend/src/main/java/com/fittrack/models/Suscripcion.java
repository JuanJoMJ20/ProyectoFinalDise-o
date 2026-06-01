package com.fittrack.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "suscripciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Suscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipoPlan;
    
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    
    @Temporal(TemporalType.DATE)
    private Date fechaFin;

    @OneToMany(mappedBy = "suscripcion", cascade = CascadeType.ALL)
    private List<Transaccion> transacciones;

    @OneToOne(mappedBy = "suscripcion")
    private Usuario usuario;
    
    public boolean estaActiva() {
        if (fechaFin == null) {
            return false;
        }
        Date hoy = new Date();
        return hoy.before(fechaFin) || hoy.equals(fechaFin);
    }
}
