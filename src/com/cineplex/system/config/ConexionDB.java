package com.cineplex.system.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:mysql://localhost:3306/Cineplex_IN4AV";
    private static final String USER = "IN4AV";
    private static final String PASSWORD = "&mnid4AV";

    public static Connection getConnection() {

        Connection conexion = null;

        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a MySQL");
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }

        return conexion;
    }
}