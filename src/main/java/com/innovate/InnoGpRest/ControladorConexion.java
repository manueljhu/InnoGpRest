package com.innovate.InnoGpRest;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import org.ini4j.Wini;

@Configuration
public class ControladorConexion {

    private final String nombreFicheroIni = "InnoGpbREST.ini";
    private String ipServidor = "";
    private String instancia = "";
    private String usuario = "";
    private String contrasena = "";
    private String nombreBBDD = "";

    @Bean
    public DataSource dataSource(){
        if (this.compruebaFichero()) {

            this.leerFicheroIni();
        } else {
            System.exit(0);
        }

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
        dataSource.setUrl("jdbc:jtds:sqlserver://" + ipServidor + "/" + nombreBBDD + ";instance=" + instancia);
        dataSource.setUsername(usuario);
        dataSource.setPassword(contrasena);
        return dataSource;
    }

    @Bean
    private boolean leerFicheroIni() {
		boolean relleno = false;
		try {
			File archivoIni = new File(nombreFicheroIni);
			Wini ini = new Wini(archivoIni);

			this.usuario = ini.get("base_de_datos", "usuario");
			this.contrasena = ini.get("base_de_datos", "contraseña");
			this.nombreBBDD = ini.get("base_de_datos", "nombre");
			this.ipServidor = ini.get("servidor", "ip");
			this.instancia = ini.get("servidor", "instancia");
		} catch (Exception e) {
			System.out.println("Error al leer el fichero de configuración");
		}
		return relleno;
	}

	private boolean compruebaFichero() {
		File archivoIni = new File(nombreFicheroIni);

		return archivoIni.exists();
	}
}
