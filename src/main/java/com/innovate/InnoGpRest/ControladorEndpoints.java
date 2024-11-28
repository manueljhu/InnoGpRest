package com.innovate.InnoGpRest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.ini4j.Wini;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.batch.BatchProperties.Jdbc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/restbusiness")
public class ControladorEndpoints {

    private final ControladorConsultas controladorConsultas;
    private String nombreFicheroIni =  "InnoGpbrest.ini";
    private String apiKey = "";
    private final String mensajeAPIKEY = "La API_KEY introducida no es válida. Acceso denegado.";

    public ControladorEndpoints(JdbcTemplate jdbcTemplate) {
        this.controladorConsultas = new ControladorConsultas(jdbcTemplate);
    }

    @PostConstruct
    public void leerApiKey(){
        try {
            File iniFile = new File(nombreFicheroIni);
            Wini ini = new Wini(iniFile);
            apiKey = ini.get("rest", "apikey");
        } catch (Exception e) {
            System.out.println("Error al leer el fichero de configuración");
        }
    }

    @PostMapping("/testConexion")
    public Object testConexion(@RequestHeader(value = "apikey", required = true) String apikey) {
        Object respuesta;

        if (apikey.equals(this.apiKey)) {
            respuesta = controladorConsultas.testConexion();
        } else {
            Map<String, Object> res = new HashMap<>();
            res.put("code", 0);
            res.put("message", mensajeAPIKEY);
            respuesta = res;
        }
        return respuesta;
    }

    @PostMapping("/getDatos")
    public Object getDatos(@RequestHeader(value = "apikey", required = true) String apikey, @RequestBody Map<String, String> request){
        String sql = request.get("sql");
        Object respuesta;

        if (apikey.equals(this.apiKey)){
            respuesta = controladorConsultas.devuelveDatos(sql);
        } else {
            Map<String, Object> res = new HashMap<>();
            res.put("code", 0);
            res.put("message", mensajeAPIKEY);
            respuesta = res;
        }
        return respuesta;
    }

    @PostMapping("/getDato")
    public Object getDato(@RequestHeader(value = "apikey", required = true) String apikey, @RequestBody Map<String, String> request){
        String sql = request.get("sql");
        Object respuesta;

        if (apikey.equals(this.apiKey)){
            respuesta = controladorConsultas.devuelveDato(sql);
        } else {
            Map<String, Object> res = new HashMap<>();
            res.put("code", 0);
            res.put("message", mensajeAPIKEY);
            respuesta = res;
        }
        return respuesta;
    }

    @PostMapping("/cudDatos")
    public Object cudDatos(@RequestHeader(value = "apikey", required = true) String apikey, @RequestBody Map<String, String> request) {
        String sql = request.get("sql");
        Object respuesta;
        Map<String, Object> res;

        if (apikey.equals(this.apiKey)) {
            res = controladorConsultas.ejecutarSentencia(sql);
            int resultado = Integer.parseInt(res.get("code").toString());

            if (resultado >= 0) {
                res.put("code", 1);
            }
        } else {
            res = new HashMap<>();
            res.put("code", 0);
            res.put("message", mensajeAPIKEY);
        }

        respuesta = res;
        return respuesta;
    }
}
