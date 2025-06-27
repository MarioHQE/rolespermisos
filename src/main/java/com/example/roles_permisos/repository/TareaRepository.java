/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.roles_permisos.repository;

/**
 *
 * @author PedroCoronado
 */
import com.example.roles_permisos.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    // 1. Tareas asignadas a un empleado específico
    @Query("SELECT t FROM Tarea t WHERE t.empleado.id = :empleadoId")
    List<Tarea> findTareasByEmpleadoId(Long empleadoId);

    // 2. Tareas por estado
    @Query("SELECT t FROM Tarea t WHERE t.estado = :estado")
    List<Tarea> findTareasByEstado(String estado);

    // 3. Cantidad de tareas por empleado (Agrupación)
    @Query("SELECT t.empleado.nombre, COUNT(t) FROM Tarea t GROUP BY t.empleado.nombre")
    List<Object[]> countTareasByEmpleado();

    // 4. Tareas por fecha exacta
    @Query("SELECT t FROM Tarea t WHERE t.fecha = :fecha")
    List<Tarea> findTareasByFecha(LocalDate fecha);

    // 5. Tareas en un rango de fechas
    @Query("SELECT t FROM Tarea t WHERE t.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<Tarea> findTareasByRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);
}
