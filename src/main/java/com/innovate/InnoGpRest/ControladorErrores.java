package com.innovate.InnoGpRest;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControladorErrores {

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, String>> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        Map<String, String> respuestaError = new HashMap<>();

        respuestaError.put("code", "-1");
        respuestaError.put("message", "No se ha introducido ninguna API_KEY. Acceso denegado.");

        return new ResponseEntity<>(respuestaError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Map<String, String>> handleSQLException(SQLException ex) {
        Map<String, String> respuestaError = new HashMap<>();

        respuestaError.put("code", "-4");
        respuestaError.put("message", "Error al ejecutar la sentencia. Error: " + ex.getMessage());

        return new ResponseEntity<>(respuestaError, HttpStatus.BAD_REQUEST);
    }
}
