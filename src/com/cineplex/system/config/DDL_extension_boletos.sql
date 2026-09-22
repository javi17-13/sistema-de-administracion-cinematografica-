-- ============================================================
-- Extensión de DDL Cineplex.sql para la logica de negocio de boletos.
-- Agregar esto DESPUES de la tabla Peliculas ya existente.
-- ============================================================

CREATE TABLE Clientes (
ID_Cliente INT(25) AUTO_INCREMENT PRIMARY KEY,
Nombre VARCHAR(100) NOT NULL,
Correo VARCHAR(100)
);

CREATE TABLE Salas (
ID_Sala INT(25) AUTO_INCREMENT PRIMARY KEY,
Nombre VARCHAR(50) NOT NULL,
Filas INT(5) NOT NULL,
Columnas INT(5) NOT NULL
);

CREATE TABLE Funciones (
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
CREATE TABLE Boletos (
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

-- Una sala de ejemplo para poder probar (8 filas x 8 columnas = 64 asientos)
INSERT INTO Salas (Nombre, Filas, Columnas) VALUES ('Sala 1', 8, 8);

-- Ejemplo de como agregar una funcion (ajusta ID_Pelicula al que ya
-- tengas registrado en tu tabla Peliculas; corre "SELECT * FROM Peliculas;"
-- para ver los IDs disponibles):
-- INSERT INTO Funciones (ID_Pelicula, ID_Sala, Fecha, Hora, Precio)
-- VALUES (1, 1, '2026-09-25', '19:00:00', 35.00);
