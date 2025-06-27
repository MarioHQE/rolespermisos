package com.example.roles_permisos.controller;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class manejador_errores {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleValidationException(ConstraintViolationException ex) {
        String errores = ex.getConstraintViolations()
                .stream()
                .map((c) -> c.getMessage())
                .collect(Collectors.joining(" | "));
        return ResponseEntity.badRequest().body("Errores de validación: " + errores);
    }

}
