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
import com.example.roles_permisos.service.EmpleadoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/empleados")
public class EmpleadoController {
    @Autowired
    private EmpleadoService empleadoService;

    // Listar empleados (Accesible por ADMIN y COORDINADOR)
    @Secured({ "ROLE_ADMIN", "ROLE_COORDINADOR" })
    @GetMapping
    public List<Empleado> listarEmpleados() {
        return empleadoService.findAll();

    }

    @GetMapping("/{id}")
    public Empleado empleado(@PathVariable Long id) {
        return empleadoService.findById(id);
    }

    // Procesar creación de empleado (Solo ADMIN)
    @Secured("ROLE_ADMIN")
    @PostMapping("/crear")
    public String crearEmpleado(@RequestBody Empleado empleado) {
        empleadoService.registrarEmpleado(empleado);
        return "redirect:/empleados";
    }

    // Procesar actualización de empleado (Solo ADMIN)
    @Secured("ROLE_ADMIN")
    @PostMapping("/editar/{id}")
    public ResponseEntity<String> actualizarEmpleado(@PathVariable Long id, @RequestBody Empleado empleado) {
        empleado.setId(id);
        empleadoService.save(empleado);
        return ResponseEntity.ok().body("Empleado actualizado correctamente.");
    }

    // Eliminar empleado (Solo ADMIN)
    @Secured("ROLE_ADMIN")
    @GetMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable Long id) {
        empleadoService.deleteById(id);
        return "redirect:/empleados";
    }
}