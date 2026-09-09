USE Cineplex_IN4AV;

#USUARIOS

#CREAR USUARIOS

DELIMITER $$
	CREATE PROCEDURE sp_Crear_Usuarios( IN Nombre_P VARCHAR(100),
		IN Usuario_P VARCHAR(100), IN Clave_P VARCHAR(20), IN Rol_P VARCHAR(50)
	)
	BEGIN
		INSERT INTO Usuarios(Nombre, Usuario, Clave, Rol)
		VALUES (Nombre_P, Usuario_P, Clave_P, Rol_P);
	END$$
DELIMITER ;

#LEER USUARIOS

DELIMITER $$
CREATE PROCEDURE sp_Leer_Usuarios()
BEGIN
	SELECT * FROM Usuarios;
END$$
DELIMITER ;

#EDITAR USUARIOS

DELIMITER $$
	CREATE PROCEDURE sp_Editar_Usuarios(IN ID_Usuario_P INT(25),
		IN Nombre_P VARCHAR(100), IN Usuario_P VARCHAR(100), IN Clave_P VARCHAR(20), IN Rol_P VARCHAR(50)
	)
	BEGIN
		UPDATE Usuarios
			SET Nombre = Nombre_P,
				Usuario = Usuario_P,
                Clave = Clave_P,
                Rol = Rol_P
			WHERE ID_Usuario = ID_Usuario_P;
	END$$
DELIMITER ;

#ELIMINAR USUARIOS
DELIMITER $$
	CREATE PROCEDURE sp_Eliminar_Usuarios( IN ID_Usuario_P INT(25)
	)
	BEGIN
		DELETE FROM Usuarios
		WHERE ID_Usuario = ID_Usuario_P;
	END$$
DELIMITER ;

#BUSCAR USUARIOS

DELIMITER $$
	CREATE PROCEDURE sp_Buscar_Usuarios(IN ID_Usuario_P INT(25)
    )
    BEGIN
		SELECT * FROM Usuarios
        WHERE ID_Usuario = ID_Usuario_P;
    END$$
DELIMITER ;

#PELICULAS

#CREAR PELICULAS

DELIMITER $$
	CREATE PROCEDURE sp_Crear_Peliculas(IN Titulo_P VARCHAR(100), IN Genero_P VARCHAR(70), 
		IN Duracion_P INT(255), IN Categoria_P VARCHAR(70), IN Director_P VARCHAR(100), IN Poster_P VARCHAR(250)
    )
    BEGIN
		INSERT INTO Peliculas (Titulo, Genero, Duracion, Categoria, Director, Poster)
        VALUES (Titulo_P, Genero_P, Duracion_P, Categoria_P, Director_P, Poster_P);
    END$$
DELIMITER ;

#LEER PELICULAS

DELIMITER $$
	CREATE PROCEDURE sp_Leer_Peliculas(
    )
    BEGIN
		SELECT * FROM Peliculas;
    END$$
DELIMITER ;

#EDITAR PELICULAS

DELIMITER $$
	CREATE PROCEDURE sp_Editar_Peliculas(IN ID_Pelicula_P INT(25), IN Titulo_P VARCHAR(100), IN Genero_P VARCHAR(70),
		IN Duracion_P INT(255), IN Categoria_P VARCHAR(70), IN Director_P VARCHAR(100), IN Poster_P VARCHAR(250)
    )
	BEGIN
		UPDATE Peliculas
        SET Titulo = Titulo_P,
			Genero = Genero_P,
            Duracion = Duracion_p,
            Categoria = Categoria_P,
            Director = Director_P,
            Poster = Poster_P
		WHERE ID_Pelicula = ID_Pelicula_P;
    END$$
DELIMITER ;

#ELIMINAR PELICULAS

DELIMITER $$
	CREATE PROCEDURE sp_Eliminar_Peliculas(IN ID_Pelicula_P INT(25)
    )
    BEGIN
		DELETE FROM Peliculas
        WHERE ID_Pelicula = ID_Pelicula_P;
    END$$
DELIMITER ;

#BUSCAR PELICULAS

DELIMITER $$
	CREATE PROCEDURE sp_Buscar_Peliculas( IN ID_Pelicula_P INT(25)
    )
    BEGIN
		SELECT * FROM Peliculas
        WHERE ID_Pelicula = ID_Pelicula_P;
    END$$
DELIMITER ;

#CALLS

#USUARIOS

CALL sp_Crear_Usuarios();
CALL sp_Leer_Usuarios();
CALL sp_Editar_Usuarios();
CALL sp_Eliminar_Usuarios();
CALL sp_Buscar_Usuarios();

#PELICULAS

CALL sp_Crear_Peliculas();
CALL sp_Leer_Peliculas();
CALL sp_Editar_Peliculas();
CALL sp_Eliminar_Peliculas();
CALL sp_Buscar_Peliculas();

