package com.example.roles_permisos.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TareaResponse {
    private Long id;
    private String descripcion;
    private String estado;
    private LocalDate fecha;
    private String empleado;

}
