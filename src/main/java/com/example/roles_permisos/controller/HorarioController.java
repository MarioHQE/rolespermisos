/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.roles_permisos.controller;

import com.example.roles_permisos.model.Empleado;
import com.example.roles_permisos.model.Horario;
import com.example.roles_permisos.repository.EmpleadoRepository;
import com.example.roles_permisos.repository.HorarioRepository;
import com.example.roles_permisos.request.horarioasignarrequest;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author PedroCoronado
 */
@RestController
@RequestMapping("/horarios")
public class HorarioController {

    private final HorarioRepository horarioRepository;
    private final EmpleadoRepository empleadoRepository;

    public HorarioController(HorarioRepository horarioRepository, EmpleadoRepository empleadoRepository) {
        this.horarioRepository = horarioRepository;
        this.empleadoRepository = empleadoRepository;
    }

    // ADMIN puede asignar si el empleado tiene < 5 tareas
    @Secured("ROLE_ADMIN")
    @Transactional
    @PostMapping("/asignar")
    public ResponseEntity<String> asignarHorario(@Valid @RequestBody horarioasignarrequest horario) {
        Empleado empleado = empleadoRepository.findById(horario.getEmpleadoId())
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));
        Horario horario2 = new Horario();
        if (empleado.getTareas().size() >= 5) {
            throw new IllegalStateException("Empleado ya tiene 5 tareas asignadas.");
        }

        horario2.setEmpleado(empleado);
        horario2.setDia(horario.getDia());
        horario2.setHoraFin(horario.getHoraFin());
        horario2.setHoraInicio(horario.getHoraInicio());
        horarioRepository.save(horario2);
        return ResponseEntity.ok().body("Horario asignado correctamente.");
    }

    // COORDINADOR y SECRETARIO: ver todos los horarios
    @Secured({ "ROLE_COORDINADOR", "ROLE_SECRETARIO" })
    @GetMapping
    public List<Horario> listarHorarios() {
        return horarioRepository.findAll();

    }

    @Secured("ROLE_COORDINADOR")
    @PostMapping("/editar/{id}")
    public ResponseEntity<String> guardarEdicion(@PathVariable Long id, @RequestBody Horario horarioActualizado) {
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido"));

        horario.setDia(horarioActualizado.getDia());
        horario.setHoraInicio(horarioActualizado.getHoraInicio());
        horario.setHoraFin(horarioActualizado.getHoraFin());

        horarioRepository.save(horario);
        return ResponseEntity.ok("Horario actualizado correctamente");
    }
}
