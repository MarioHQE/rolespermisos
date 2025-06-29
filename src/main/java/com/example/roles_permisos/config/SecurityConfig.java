/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.roles_permisos.config;

import com.example.roles_permisos.service.EmpleadoDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

/**
 *
 * @author PedroCoronado
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private final EmpleadoDetailsService empleadoDetailsService;
    @Autowired
    private jwtfilter jwtfilter;

    public SecurityConfig(EmpleadoDetailsService empleadoDetailsService) {
        this.empleadoDetailsService = empleadoDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(t -> t.configurationSource(request -> corsConfiguration())).csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/static/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/coordinador/**").hasRole("COORDINADOR")
                        .requestMatchers("/secretario/**").hasRole("SECRETARIO")
                        .requestMatchers("/empleados/**").hasAnyRole("ADMIN", "COORDINADOR", "SECRETARIO")
                        .requestMatchers("/horarios/asignar").hasRole("ADMIN")
                        .requestMatchers("/horarios/editar/**").hasRole("COORDINADOR")
                        .requestMatchers("/horarios").hasAnyRole("COORDINADOR", "SECRETARIO", "ADMIN")
                        .anyRequest().authenticated())
                .userDetailsService(empleadoDetailsService)
                .addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class); // ¡Aquí es donde debe ir!

        return http.build();
    }

    @Bean
    public CorsConfiguration corsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:5173");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}