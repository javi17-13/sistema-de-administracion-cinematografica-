package com.cineplex.system.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexionDB {

    private static ConexionDB instanciaConexionDB;
    private Connection connection;

    private ConexionDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + Environment.LOCATION_SERVICE + "/" + Environment.DATA_BASE,
                    Environment.USER,
                    Environment.PASSWORD);
            System.out.println("Conexión exitosa a MySQL");
        } catch (ClassNotFoundException classNotFound) {
            System.out.println("Error: no se encontro el driver de MySQL");
        } catch (SQLException sqlException) {
            System.out.println("Error al conectar con la base de datos: " + sqlException.getMessage());
        }
    }

    public static ConexionDB getInstanciaConexionDB() {
        if (instanciaConexionDB == null) {
            instanciaConexionDB = new ConexionDB();
        }
        return instanciaConexionDB;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
