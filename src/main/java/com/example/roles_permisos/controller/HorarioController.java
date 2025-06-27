/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.roles_permisos.controller;

import com.example.roles_permisos.model.Empleado;
import com.example.roles_permisos.model.Horario;
import com.example.roles_permisos.repository.EmpleadoRepository;
import com.example.roles_permisos.repository.HorarioRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author PedroCoronado
 */
@Controller
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
    public String asignarHorario(@ModelAttribute Horario horario, @RequestParam Long empleadoId) {
        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));

        if (empleado.getTareas().size() >= 5) {
            throw new IllegalStateException("Empleado ya tiene 5 tareas asignadas.");
        }

        horario.setEmpleado(empleado);
        horarioRepository.save(horario);
        return "redirect:/horarios";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/asignar")
    public String listaasignar(Model model) {
        model.addAttribute("horario", new Horario());
        model.addAttribute("empleados", empleadoRepository.findAll());
        return "horarios/asignar";
    }

    // COORDINADOR y SECRETARIO: ver todos los horarios
    @Secured({ "ROLE_COORDINADOR", "ROLE_SECRETARIO" })
    @GetMapping
    public String listarHorarios(Model model) {
        model.addAttribute("horarios", horarioRepository.findAll());
        return "horarios/listar";
    }

    // COORDINADOR puede editar
    @Secured("ROLE_COORDINADOR")
    @GetMapping("/editar/{id}")
    public String editarHorario(@PathVariable Long id, Model model) {
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("horario", horario);
        return "horarios/editar";
    }

    @Secured("ROLE_COORDINADOR")
    @PostMapping("/editar/{id}")
    public String guardarEdicion(@PathVariable Long id, @ModelAttribute Horario horarioActualizado) {
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido"));

        horario.setDia(horarioActualizado.getDia());
        horario.setHoraInicio(horarioActualizado.getHoraInicio());
        horario.setHoraFin(horarioActualizado.getHoraFin());

        horarioRepository.save(horario);
        return "redirect:/horarios";
    }
}
