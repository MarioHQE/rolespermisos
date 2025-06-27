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
import com.example.roles_permisos.model.Rol;
import com.example.roles_permisos.service.EmpleadoService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    // Listar empleados (Accesible por ADMIN y COORDINADOR)
    @Secured({ "ROLE_ADMIN", "ROLE_COORDINADOR" })
    @GetMapping
    public String listarEmpleados(Model model) {
        model.addAttribute("empleados", empleadoService.findAll());
        return "empleados/listar";
    }

    // Mostrar formulario de creación (Solo ADMIN)
    @Secured("ROLE_ADMIN")
    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("empleado", new Empleado());
        model.addAttribute("roles", Rol.values());
        return "empleados/crear";
    }

    // Procesar creación de empleado (Solo ADMIN)
    @Secured("ROLE_ADMIN")
    @PostMapping("/crear")
    public String crearEmpleado(@ModelAttribute Empleado empleado) {
        empleadoService.registrarEmpleado(empleado);
        return "redirect:/empleados";
    }

    // Mostrar formulario de edición (Solo ADMIN)
    @Secured("ROLE_ADMIN")
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Empleado empleado = empleadoService.findById(id);
        model.addAttribute("empleado", empleado);
        model.addAttribute("roles", Rol.values());
        return "empleados/editar";
    }

    // Procesar actualización de empleado (Solo ADMIN)
    @Secured("ROLE_ADMIN")
    @PostMapping("/editar/{id}")
    public String actualizarEmpleado(@PathVariable Long id, @ModelAttribute Empleado empleado) {
        empleado.setId(id);
        empleadoService.save(empleado);
        return "redirect:/empleados";
    }

    // Eliminar empleado (Solo ADMIN)
    @Secured("ROLE_ADMIN")
    @GetMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable Long id) {
        empleadoService.deleteById(id);
        return "redirect:/empleados";
    }
}