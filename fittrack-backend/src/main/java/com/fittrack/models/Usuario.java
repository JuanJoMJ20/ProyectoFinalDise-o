package com.fittrack.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    
    private float pesoActual;
    
    private float metaPeso;

    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "suscripcion_id", referencedColumnName = "id")
    private Suscripcion suscripcion;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<PlanEntrenamiento> planesEntrenamiento;
}
