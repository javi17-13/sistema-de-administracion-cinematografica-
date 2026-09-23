-- ============================================================
-- Extension de DML Cineplex.sql para la logica de negocio de boletos.
-- Agregar esto DESPUES de los procedimientos de Peliculas ya existentes,
-- antes del bloque #CALLS.
-- ============================================================

#CLIENTES

#CREAR CLIENTE

DELIMITER $$
	CREATE PROCEDURE sp_Crear_Cliente(IN Nombre_P VARCHAR(100), IN Correo_P VARCHAR(100)
	)
	BEGIN
		INSERT INTO Clientes (Nombre, Correo)
		VALUES (Nombre_P, Correo_P);
        SELECT LAST_INSERT_ID() AS ID_Cliente;
	END$$
DELIMITER ;

#FUNCIONES

#OBTENER TODAS LAS FUNCIONES (con pelicula y sala ya resueltas)

DELIMITER $$
	CREATE PROCEDURE sp_Obtener_Funciones(
    )
    BEGIN
		SELECT f.ID_Funcion, f.Fecha, f.Hora, f.Precio,
			p.ID_Pelicula, p.Titulo, p.Genero, p.Duracion, p.Categoria, p.Poster,
            s.ID_Sala, s.Nombre AS NombreSala, s.Filas, s.Columnas
		FROM Funciones f
        INNER JOIN Peliculas p ON p.ID_Pelicula = f.ID_Pelicula
        INNER JOIN Salas s ON s.ID_Sala = f.ID_Sala
        ORDER BY f.Fecha, f.Hora;
    END$$
DELIMITER ;

#BUSCAR FUNCIONES POR TITULO DE PELICULA (US-09)

DELIMITER $$
	CREATE PROCEDURE sp_Buscar_Funcion_Por_Titulo(IN Titulo_P VARCHAR(100)
    )
    BEGIN
		SELECT f.ID_Funcion, f.Fecha, f.Hora, f.Precio,
			p.ID_Pelicula, p.Titulo, p.Genero, p.Duracion, p.Categoria, p.Poster,
            s.ID_Sala, s.Nombre AS NombreSala, s.Filas, s.Columnas
		FROM Funciones f
        INNER JOIN Peliculas p ON p.ID_Pelicula = f.ID_Pelicula
        INNER JOIN Salas s ON s.ID_Sala = f.ID_Sala
        WHERE p.Titulo LIKE CONCAT('%', Titulo_P, '%')
        ORDER BY f.Fecha, f.Hora;
    END$$
DELIMITER ;

#BOLETOS

#ASIENTOS YA VENDIDOS DE UNA FUNCION (para pintar el mapa de asientos)

DELIMITER $$
	CREATE PROCEDURE sp_Obtener_Asientos_Ocupados(IN ID_Funcion_P INT(25)
    )
    BEGIN
		SELECT Asiento FROM Boletos
        WHERE ID_Funcion = ID_Funcion_P;
    END$$
DELIMITER ;

#CREAR BOLETO (la venta en si)

DELIMITER $$
	CREATE PROCEDURE sp_Crear_Boleto(IN ID_Funcion_P INT(25), IN ID_Cliente_P INT(25),
		IN Asiento_P VARCHAR(5), IN ContenidoQR_P VARCHAR(500)
    )
    BEGIN
		INSERT INTO Boletos (ID_Funcion, ID_Cliente, Asiento, ContenidoQR)
        VALUES (ID_Funcion_P, ID_Cliente_P, Asiento_P, ContenidoQR_P);
        SELECT LAST_INSERT_ID() AS ID_Boleto;
    END$$
DELIMITER ;


select * from peliculas;