package com.cineplex.system.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton que administra la UNICA conexion a MySQL de toda la
 * aplicacion. Los repositorios piden la conexion aqui en vez de abrir
 * una propia cada uno, asi no se dejan conexiones sueltas.
 *
 * Requiere el conector de MySQL (mysql-connector-j) agregado a las
 * librerias del proyecto en NetBeans.
 */
public class ConexionDB {

    private static ConexionDB instanciaConexionDB;
    private Connection conexion;

    private ConexionDB() {
    }

    public static ConexionDB getInstanciaConexionDB() {
        if (instanciaConexionDB == null) {
            instanciaConexionDB = new ConexionDB();
        }
        return instanciaConexionDB;
    }

    public Connection getConnection() {
        try {
            //se reusa la conexion mientras siga viva; si se cerro, se abre otra
            if (conexion == null || conexion.isClosed()) {
                String url = "jdbc:mysql://" + Enviroment.LOCATION_SERVICE + "/" + Enviroment.DATA_BASE;
                conexion = DriverManager.getConnection(url, Enviroment.USER, Enviroment.PASSWORD);
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return conexion;
    }

    public void closeConnection() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexion: " + e.getMessage());
        }
    }
}
