/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.roles_permisos.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author PedroCoronado
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "Login";
    }

    @GetMapping("/home")
    public String home(Authentication authentication) {
        String rol = authentication.getAuthorities().stream()
                .findFirst()
                .orElseThrow()
                .getAuthority();

        return switch (rol) {
            case "ROLE_ADMIN" -> "redirect:/admin/dashboard";
            case "ROLE_COORDINADOR" -> "redirect:/coordinador/dashboard";
            case "ROLE_SECRETARIO" -> "redirect:/secretario/dashboard";
            default -> "redirect:/Login";
        };
    }

    @GetMapping("/")
    public String redirigirPorRol(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        String rol = authentication.getAuthorities().iterator().next().getAuthority();
        return switch (rol) {
            case "ROLE_ADMIN" -> "redirect:/empleados";
            case "ROLE_COORDINADOR", "ROLE_SECRETARIO" -> "redirect:/tareas";
            default -> "redirect:/Login";
        };
    }
}
