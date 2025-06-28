package com.example.roles_permisos.request;

import java.time.LocalDate;

import com.example.roles_permisos.model.Empleado;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Tareasignarrequest {

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(min = 5, max = 200, message = "La descripción debe tener entre 5 y 200 caracteres")
    private String descripcion;

    @NotBlank(message = "El estado no puede estar vacío")
    private String estado;

    @NotNull(message = "La fecha no puede estar vacía")
    @FutureOrPresent(message = "La fecha debe ser hoy o en el futuro")
    private LocalDate fecha;

    @NotNull(message = "Debe asignarse a un empleado")
    private long empleadoId;
}
