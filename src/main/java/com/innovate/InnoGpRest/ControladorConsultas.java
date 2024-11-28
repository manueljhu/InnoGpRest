package com.innovate.InnoGpRest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

public class ControladorConsultas {

    private final JdbcTemplate jdbcTemplate;

    public ControladorConsultas(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    

    public ResponseEntity<Object> testConexion() {
        Map<String, Object> respuesta = new HashMap<>();
        try {
            // Ejecutar una consulta simple para probar la conexión
            jdbcTemplate.execute("SELECT 1");

            respuesta.put("code", 1);
            respuesta.put("message", "La conexión con la base de datos externa se estableció correctamente.");

            // Si la consulta fue exitosa, devolver un mensaje de éxito
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            // Si hay un error, devolver un mensaje de error con código 500
            respuesta.put("code", -3);
            respuesta.put("message", "Error al conectar con la base de datos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(respuesta);
        }
    }

    public Object devuelveDato(String sql) {
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Map<String, Object> resultado = new HashMap<>();
            int columnCount = rs.getMetaData().getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = rs.getMetaData().getColumnName(i);
                Object columnValue = rs.getObject(i);
                resultado.put(columnName, columnValue);
            }

            return resultado;
        });
    }

    public List<Map<String, Object>> devuelveDatos(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    public Map<String, Object> ejecutarSentencia(String sql) {
        Map<String, Object> respuesta = new HashMap<>();

        int resultado = jdbcTemplate.update(sql);
        respuesta.put("code", resultado);
        return respuesta;

    }
}
