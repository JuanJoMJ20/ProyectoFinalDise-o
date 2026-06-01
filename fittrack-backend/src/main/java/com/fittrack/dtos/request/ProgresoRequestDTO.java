package com.fittrack.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgresoRequestDTO {

    @NotNull(message = "El peso actual es obligatorio")
    @Positive(message = "El peso actual debe ser mayor a 0")
    private Float pesoActual;

}
