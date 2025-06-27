package com.example.roles_permisos.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.roles_permisos.config.jwtil;
import com.example.roles_permisos.model.Empleado;
import com.example.roles_permisos.repository.EmpleadoRepository;
import com.example.roles_permisos.service.EmpleadoDetailsService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    EmpleadoRepository empleadodao;
    @Autowired
    jwtil jwtutil;
    @Autowired
    EmpleadoDetailsService empleadoDetailsService;

    @PostMapping("/login")
    public ResponseEntity<String> autenticar(@RequestBody Map<String, String> login) {
        String email = login.get("email");
        String password = login.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body("Faltan parámetros");
        }

        Empleado empleado = empleadodao.findByEmail(email);
        if (empleado != null && passwordEncoder.matches(password, empleado.getPassword())) {
            UserDetails user = empleadoDetailsService.loadUserByUsername(email);
            String token = jwtutil.generateToken(user);
            return ResponseEntity.ok().body(token);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
    }

}