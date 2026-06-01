package com.fittrack.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RutinaRequestDTO {

    @NotNull(message = "El id del plan de entrenamiento es obligatorio")
    private Long planId;
}
