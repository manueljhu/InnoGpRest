package com.innovate.InnoGpRest;

import java.io.File;
import java.util.Collections;
import java.io.IOException;

import org.ini4j.Wini;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class InnoGpRestApplication extends SpringBootServletInitializer {
	private static String nombreFicheroIni = "InnoGpbREST.ini";
	private static String puerto = "9000";
	private static String apiKey = "";

	public static void main(String[] args) {
		if (compruebaFichero()){
			if (compruebaCredenciales()){
				SpringApplication app = new SpringApplication(InnoGpRestApplication.class);
				app.setDefaultProperties(Collections.singletonMap("server.port", puerto));
				app.run(args);
			}
		} else {
			crearFichero();
		}
	}

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(InnoGpRestApplication.class).properties("server.port="+puerto);
    }

	private static void crearFichero() {
		try {
			File archivoIni = new File(nombreFicheroIni);
			Wini ini;
            if (!archivoIni.exists()) {
                archivoIni.createNewFile();
                ini = new Wini(archivoIni);
            } else {
                ini = new Wini(archivoIni);
            }

            // Añadir secciones y claves con sus valores
            ini.put("base_de_datos", "usuario", "");
            ini.put("base_de_datos", "contraseña", "");
            ini.put("base_de_datos", "nombre", "");
            ini.put("servidor", "ip", "");
            ini.put("servidor", "instancia", "");
            ini.put("rest", "puerto", "");
            ini.put("rest", "apikey", "");

            // Guardar el archivo
            ini.store();
		} catch (Exception e) {
			System.out.println("Error al crear el fichero de configuración");
		}
	}

	private static boolean compruebaCredenciales() {
		boolean relleno = false;
		try {
			File archivoIni = new File(nombreFicheroIni);
			Wini ini = new Wini(archivoIni);

			String usuario = ini.get("base_de_datos", "usuario");
			String contrasena = ini.get("base_de_datos", "contraseña");
			String nombreBBDD = ini.get("base_de_datos", "nombre");
			String ipServidor = ini.get("servidor", "ip");
			String instancia = ini.get("servidor", "instancia");
			puerto = ini.get("rest", "puerto");
			apiKey = ini.get("rest", "apikey");

			if (usuario != null && contrasena != null && nombreBBDD != null && ipServidor != null && instancia != null
					&& puerto != null && apiKey != null) {
				relleno = true;
			} else {
				System.out.println("Faltan credenciales en el fichero de configuración");
			}
		} catch (Exception e) {
			System.out.println("Error al leer el fichero de configuración");
		}
		return relleno;
	}

	private static boolean compruebaFichero() {
		File archivoIni = new File(nombreFicheroIni);

		return archivoIni.exists();
	}
}