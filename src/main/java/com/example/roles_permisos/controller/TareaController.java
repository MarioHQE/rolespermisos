/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.roles_permisos.controller;

/**
 *
 * @author PedroCoronado
 */
import com.example.roles_permisos.model.Empleado;
import com.example.roles_permisos.model.Tarea;
import com.example.roles_permisos.repository.EmpleadoRepository;
import com.example.roles_permisos.repository.TareaRepository;
import com.example.roles_permisos.request.Tareasignarrequest;
import com.example.roles_permisos.response.TareaResponse;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/tareas")
public class TareaController {

    private final TareaRepository tareaRepository;
    private final EmpleadoRepository empleadoRepository;

    public TareaController(TareaRepository tareaRepository, EmpleadoRepository empleadoRepository) {
        this.tareaRepository = tareaRepository;
        this.empleadoRepository = empleadoRepository;
    }

    // Listar tareas (Accesible por todos los roles)
    @Secured({ "ROLE_ADMIN", "ROLE_COORDINADOR", "ROLE_SECRETARIO" })
    @GetMapping
    public List<TareaResponse> listarTareas() {
        List<TareaResponse> tareas = new ArrayList<>();
        for (Tarea tarea : tareaRepository.findAll()) {
            tareas.add(new TareaResponse(tarea.getId(), tarea.getDescripcion(), tarea.getEstado(), tarea.getFecha(),
                    tarea.getEmpleado().getNombre()));
        }
        return tareas;

    }

    @GetMapping("/{idtarea}")
    public TareaResponse obtenerTarea(@PathVariable long idtarea) {
        tareaRepository.findById(idtarea).orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada"));
        TareaResponse tarea = new TareaResponse();
        Tarea tarea2 = tareaRepository.findById(idtarea).get();
        tarea.setId(tarea2.getId());
        tarea.setDescripcion(tarea2.getDescripcion());
        tarea.setEstado(tarea2.getEstado());
        tarea.setFecha(tarea2.getFecha());
        tarea.setEmpleado(tarea2.getEmpleado().getNombre());
        return tarea;
    }

    // Asignar tarea (Solo ADMIN, con validación transaccional)
    @Secured("ROLE_ADMIN")
    @Transactional
    @PostMapping("/asignar")
    public ResponseEntity<String> asignarTarea(@Valid @RequestBody Tareasignarrequest tarea) {
        Empleado empleado = empleadoRepository.findById(tarea.getEmpleadoId())
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));
        Tarea tarea2 = new Tarea();
        if (empleado.getTareas().size() >= 5) {
            throw new IllegalStateException("El empleado ya tiene 5 tareas asignadas.");
        }
        tarea2.setEmpleado(empleado);
        tarea2.setDescripcion(tarea.getDescripcion());
        tarea2.setEstado(tarea.getEstado());
        tarea2.setFecha(tarea.getFecha());

        tareaRepository.save(tarea2);

        return ResponseEntity.ok().body("Tarea asignada correctamente.");
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/editar/{id}")
    public ResponseEntity<String> editar(@PathVariable long id, @ModelAttribute Tarea tarea) {
        Tarea tarea2 = tareaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada"));
        tarea2.setDescripcion(tarea.getDescripcion());
        tarea2.setEstado(tarea.getEstado());
        tarea2.setEmpleado(tarea.getEmpleado());
        tarea2.setFecha(tarea.getFecha());
        tareaRepository.save(tarea2);
        return ResponseEntity.ok().body("Tarea editada correctamente.");

    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarTarea(@PathVariable Long id) {
        tareaRepository.deleteById(id);
        return ResponseEntity.ok().body("Tarea eliminada correctamente.");
    }

}
