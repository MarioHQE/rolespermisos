/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.roles_permisos.repository;

/**
 *
 * @author PedroCoronado
 */
import com.example.roles_permisos.model.Empleado;
import com.example.roles_permisos.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    // Consulta JPQL: Buscar empleado por email (para login)
    @Query("SELECT e FROM Empleado e WHERE e.email = :email")
    Empleado findByEmail(String email);

    // Consulta JPQL: Empleados con un rol específico
    @Query("SELECT e FROM Empleado e WHERE e.rol = :rol")
    List<Empleado> findByRol(Rol rol);
}