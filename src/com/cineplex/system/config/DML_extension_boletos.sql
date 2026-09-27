-- ============================================================
-- Extension de DML Cineplex.sql para la logica de negocio de boletos (US-07).
-- Ejecutar DESPUES de "DDL_extension_boletos.sql".
-- Cada procedimiento hace DROP...IF EXISTS antes de crearse, para que
-- el script se pueda volver a correr sin error "procedure already exists".
-- ============================================================

USE Cineplex_IN4AV;

#CLIENTES

#CREAR CLIENTE

DROP PROCEDURE IF EXISTS sp_Crear_Cliente;
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

DROP PROCEDURE IF EXISTS sp_Obtener_Funciones;
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

DROP PROCEDURE IF EXISTS sp_Buscar_Funcion_Por_Titulo;
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

DROP PROCEDURE IF EXISTS sp_Obtener_Asientos_Ocupados;
DELIMITER $$
	CREATE PROCEDURE sp_Obtener_Asientos_Ocupados(IN ID_Funcion_P INT(25)
    )
    BEGIN
		SELECT Asiento FROM Boletos
        WHERE ID_Funcion = ID_Funcion_P;
    END$$
DELIMITER ;

#CREAR BOLETO (la venta en si)

DROP PROCEDURE IF EXISTS sp_Crear_Boleto;
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

#DATOS DE PRUEBA

#CREAR UNA FUNCION DE PRUEBA, USANDO UNA PELICULA Y UNA SALA QUE YA EXISTAN.
#NUNCA inventa un ID_Pelicula: si no hay ninguna pelicula registrada
#(o ninguna sala), detiene la operacion con un mensaje claro en vez de
#adivinar un dato que podria no existir.

DROP PROCEDURE IF EXISTS sp_Crear_Funcion_Prueba;
DELIMITER $$
	CREATE PROCEDURE sp_Crear_Funcion_Prueba()
    BEGIN
        DECLARE v_ID_Pelicula INT DEFAULT NULL;
        DECLARE v_ID_Sala INT DEFAULT NULL;

        SELECT MIN(ID_Pelicula) INTO v_ID_Pelicula FROM Peliculas;
        SELECT MIN(ID_Sala) INTO v_ID_Sala FROM Salas;

        IF v_ID_Pelicula IS NULL THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'No hay ninguna pelicula registrada en Peliculas. Registra al menos una pelicula antes de crear una funcion de prueba.';
        ELSEIF v_ID_Sala IS NULL THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'No hay ninguna sala registrada en Salas. Corre primero DDL_extension_boletos.sql (crea la Sala 1 de 8x8).';
        ELSE
            IF NOT EXISTS (
                SELECT 1 FROM Funciones
                WHERE ID_Pelicula = v_ID_Pelicula AND ID_Sala = v_ID_Sala
                  AND Fecha = DATE_ADD(CURDATE(), INTERVAL 1 DAY) AND Hora = '19:00:00'
            ) THEN
                INSERT INTO Funciones (ID_Pelicula, ID_Sala, Fecha, Hora, Precio)
                VALUES (v_ID_Pelicula, v_ID_Sala, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '19:00:00', 35.00);
            END IF;

            SELECT v_ID_Pelicula AS ID_Pelicula_Usada, v_ID_Sala AS ID_Sala_Usada;
        END IF;
    END$$
DELIMITER ;

-- NOTA: a proposito NO se llama aqui a sp_Crear_Funcion_Prueba().
-- Verifica primero que ya tengas al menos una pelicula registrada
-- (SELECT * FROM Peliculas;) y luego corre, por separado:
--     CALL sp_Crear_Funcion_Prueba();
-- Si no hay ninguna pelicula todavia, el procedimiento te lo dira con
-- un error claro en vez de inventar un ID_Pelicula que no existe.
