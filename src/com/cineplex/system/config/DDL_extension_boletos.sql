-- Extension de DDL Cineplex.sql para la logica de negocio de boletos (US-07).
-- Ejecutar DESPUES de correr "DDL Cineplex.sql" (que ya crea Usuarios y Peliculas).
-- Este script es idempotente: se puede volver a correr sin error aunque
-- ya existan algunas o todas las tablas.

USE Cineplex_IN4AV;

CREATE TABLE IF NOT EXISTS Clientes (
    ID_Cliente INT(25) AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Correo VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS Salas (
    ID_Sala INT(25) AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(50) NOT NULL,
    Filas INT(5) NOT NULL,
    Columnas INT(5) NOT NULL
);

CREATE TABLE IF NOT EXISTS Funciones (
    ID_Funcion INT(25) AUTO_INCREMENT PRIMARY KEY,
    ID_Pelicula INT(25) NOT NULL,
    ID_Sala INT(25) NOT NULL,
    Fecha DATE NOT NULL,
    Hora TIME NOT NULL,
    Precio DECIMAL(8,2) NOT NULL,
    FOREIGN KEY (ID_Pelicula) REFERENCES Peliculas(ID_Pelicula),
    FOREIGN KEY (ID_Sala) REFERENCES Salas(ID_Sala)
);

-- La combinacion (ID_Funcion, Asiento) es UNIQUE: asi, si dos personas
-- intentan comprar el mismo asiento de la misma funcion al mismo
-- tiempo, MySQL rechaza la segunda venta el solo -- no hace falta
-- "bloquear" nada a mano, la base de datos lo garantiza.
CREATE TABLE IF NOT EXISTS Boletos (
    ID_Boleto INT(25) AUTO_INCREMENT PRIMARY KEY,
    ID_Funcion INT(25) NOT NULL,
    ID_Cliente INT(25) NOT NULL,
    Asiento VARCHAR(5) NOT NULL,
    ContenidoQR VARCHAR(500) NOT NULL,
    FechaCompra DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ID_Funcion) REFERENCES Funciones(ID_Funcion),
    FOREIGN KEY (ID_Cliente) REFERENCES Clientes(ID_Cliente),
    UNIQUE (ID_Funcion, Asiento)
);

-- Sala de prueba de 8 x 8 (64 asientos), requerida para poder probar US-07.
-- El INSERT es condicional para que correr el script dos veces no duplique la sala.
INSERT INTO Salas (Nombre, Filas, Columnas)
SELECT 'Sala 1', 8, 8
WHERE NOT EXISTS (SELECT 1 FROM Salas WHERE Nombre = 'Sala 1');
