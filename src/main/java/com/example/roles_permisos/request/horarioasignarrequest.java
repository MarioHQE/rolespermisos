package com.example.roles_permisos.request;

import com.example.roles_permisos.model.Empleado;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class horarioasignarrequest {

    private String dia;

    @NotBlank
    private String horaInicio;

    @NotBlank
    private String horaFin;

    private long empleadoId;
}
