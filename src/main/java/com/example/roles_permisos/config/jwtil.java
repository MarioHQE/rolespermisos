package com.example.roles_permisos.config;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class jwtil {
    private static final String secretKeyBase64 = "Esta llave es secreta y debe ser guardada de manera segura";

    private final SecretKey key = Keys.hmacShaKeyFor(secretKeyBase64.getBytes(StandardCharsets.UTF_8));

    // Método para generar el JWT
    public String generateToken(UserDetails userDetails) {

        return Jwts.builder().claims()
                .subject(userDetails.getUsername())
                .add("rol",
                        userDetails.getAuthorities().stream().map(t -> t.getAuthority())
                                .collect(Collectors.joining(",")))// Establecer
                // el
                // usuario
                // como sujeto
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expiración en 10 horas
                .and().signWith(key)
                .compact();

    }

    // Método para extraer el nombre de usuario (subject) del token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Método para extraer los claims del token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Método para extraer todos los claims
    private Claims extractAllClaims(String token) {

        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    // Método para validar si el token ha expirado
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Método para extraer la fecha de expiración del token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Método para validar el JWT
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
