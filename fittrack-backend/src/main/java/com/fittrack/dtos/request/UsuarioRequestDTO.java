package com.fittrack.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El peso actual es obligatorio")
    @Positive(message = "El peso actual debe ser mayor a 0")
    private Float pesoActual;

    @NotNull(message = "El peso meta es obligatorio")
    @Positive(message = "La meta de peso debe ser mayor a 0")
    private Float metaPeso;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private Date fechaNacimiento;
}
