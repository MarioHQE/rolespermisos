/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.roles_permisos.service;

/**
 *
 * @author PedroCoronado
 */

import com.example.roles_permisos.model.Empleado;
import com.example.roles_permisos.repository.EmpleadoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final PasswordEncoder passwordEncoder;

    public EmpleadoService(EmpleadoRepository empleadoRepository, PasswordEncoder passwordEncoder) {
        this.empleadoRepository = empleadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Empleado registrarEmpleado(Empleado empleado) {
        empleado.setPassword(passwordEncoder.encode(empleado.getPassword()));
        return empleadoRepository.save(empleado);
    }

    public Empleado save(Empleado empleado) {
        // Si es una actualización y no se cambió la contraseña, mantener la existente
        if (empleado.getId() != null) {
            Optional<Empleado> existing = empleadoRepository.findById(empleado.getId());
            if (existing.isPresent() && empleado.getPassword().isEmpty()) {
                empleado.setPassword(existing.get().getPassword());
            } else if (!empleado.getPassword().isEmpty()) {
                empleado.setPassword(passwordEncoder.encode(empleado.getPassword()));
            }

        }
        return empleadoRepository.save(empleado);
    }

    public Empleado findById(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));
    }

    public List<Empleado> findAll() {
        return empleadoRepository.findAll();
    }

    public void deleteById(Long id) {
        empleadoRepository.deleteById(id);
    }
}