/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.roles_permisos.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author PedroCoronado
 */
class Main {
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
        
    }
    
}
