/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.roles_permisos.controller;

/**
 *
 * @author PedroCoronado
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "dashboard/admin";
    }

    @GetMapping("/coordinador/dashboard")
    public String coordinadorDashboard() {
        return "dashboard/coordinador";
    }

    @GetMapping("/secretario/dashboard")
    public String secretarioDashboard() {
        return "dashboard/secretario";
    }
}

