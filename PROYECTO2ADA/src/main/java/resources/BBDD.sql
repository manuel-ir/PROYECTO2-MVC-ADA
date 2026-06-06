DROP DATABASE IF EXISTS mvcADA;
CREATE DATABASE mvcADA;
USE mvcADA;

-- 1. Tabla USUARIO
CREATE TABLE USUARIO (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(30) NOT NULL UNIQUE,
    email VARCHAR(50) NOT NULL UNIQUE, -- Email único
    password VARCHAR(255) NOT NULL 
);

-- 2. Tabla RUTA
CREATE TABLE RUTA (
    id_ruta INT AUTO_INCREMENT PRIMARY KEY,
    nombre_ruta VARCHAR(50) NOT NULL,
    descripcion_ruta TEXT,
    ubicacion VARCHAR(50),
    dificultad VARCHAR(20),
    tipo_actividad VARCHAR(20),
    longitud DECIMAL(10, 2),
    fecha_creacion DATE DEFAULT (CURRENT_DATE), 
    id_creador INT NOT NULL,
    FOREIGN KEY (id_creador) REFERENCES USUARIO(id_usuario) ON DELETE CASCADE
);

-- 3. Tabla LISTA
CREATE TABLE LISTA (
    id_lista INT AUTO_INCREMENT PRIMARY KEY,
    nombre_lista VARCHAR(30) NOT NULL,
    id_usuario INT NOT NULL,
    UNIQUE (nombre_lista, id_usuario),
    FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario) ON DELETE CASCADE
);

-- 4. Tabla RUTA_LISTA (relacion entre RUTA y LISTA)
CREATE TABLE RUTA_LISTA (
    id_lista INT NOT NULL,
    id_ruta  INT NOT NULL,
    PRIMARY KEY (id_lista, id_ruta),
    FOREIGN KEY (id_lista) REFERENCES LISTA(id_lista) ON DELETE CASCADE,
    FOREIGN KEY (id_ruta)  REFERENCES RUTA(id_ruta)   ON DELETE CASCADE
);

-- 5. Tabla COMENTARIO (relacion comenta entre USUARIO y RUTA)
CREATE TABLE COMENTARIO (
    id_usuario       INT NOT NULL,
    id_ruta          INT NOT NULL,
    fecha_comentario DATETIME DEFAULT CURRENT_TIMESTAMP,
    contenido        TEXT NOT NULL,
    PRIMARY KEY (id_usuario, id_ruta, fecha_comentario),
    FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_ruta)    REFERENCES RUTA(id_ruta)       ON DELETE CASCADE
);

-- 6. Tabla VALORACION
CREATE TABLE VALORACION (
    id_usuario INT,
    id_ruta INT,
    fecha_valoracion DATETIME DEFAULT CURRENT_TIMESTAMP,
    puntuacion INT NOT NULL CHECK (puntuacion BETWEEN 1 AND 5),
    comentario TEXT,
    PRIMARY KEY (id_usuario, id_ruta, fecha_valoracion),
    FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_ruta) REFERENCES RUTA(id_ruta) ON DELETE CASCADE
);

-- Procedimientos almacenados

DELIMITER //

-- Calcula la valoracion media de una ruta
CREATE PROCEDURE calcularMediaValoracion(IN p_id_ruta INT, OUT p_media DOUBLE)
BEGIN
    SELECT ROUND(AVG(puntuacion), 1) INTO p_media
    FROM VALORACION WHERE id_ruta = p_id_ruta;
END //

-- Borra una ruta solo si el usuario es su creador
CREATE PROCEDURE borrarRutaSiEsCreador(IN p_id_ruta INT, IN p_id_usuario INT, OUT p_resultado TINYINT)
BEGIN
    DECLARE v_creador INT;
    SELECT id_creador INTO v_creador FROM RUTA WHERE id_ruta = p_id_ruta;
    IF v_creador = p_id_usuario THEN
        DELETE FROM RUTA WHERE id_ruta = p_id_ruta;
        SET p_resultado = 1;
    ELSE
        SET p_resultado = 0;
    END IF;
END //

-- Agrega una ruta a una lista verificando que la lista pertenece al usuario
CREATE PROCEDURE agregarRutaALista(IN p_id_lista INT, IN p_id_ruta INT, IN p_id_usuario INT, OUT p_resultado TINYINT)
BEGIN
    DECLARE v_owner INT;
    SELECT id_usuario INTO v_owner FROM LISTA WHERE id_lista = p_id_lista;
    IF v_owner = p_id_usuario THEN
        INSERT IGNORE INTO RUTA_LISTA (id_lista, id_ruta) VALUES (p_id_lista, p_id_ruta);
        SET p_resultado = 1;
    ELSE
        SET p_resultado = 0;
    END IF;
END //

-- Triggers

-- Impide que un usuario valore su propia ruta y que valore más de una vez la misma
CREATE TRIGGER before_insert_valoracion
BEFORE INSERT ON VALORACION
FOR EACH ROW
BEGIN
    DECLARE v_creador INT;
    DECLARE v_count INT;
    SELECT id_creador INTO v_creador FROM RUTA WHERE id_ruta = NEW.id_ruta;
    IF v_creador = NEW.id_usuario THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No puedes valorar tu propia ruta';
    END IF;
    SELECT COUNT(*) INTO v_count FROM VALORACION WHERE id_usuario = NEW.id_usuario AND id_ruta = NEW.id_ruta;
    IF v_count > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ya has valorado esta ruta';
    END IF;
END //

-- Comprueba que la longitud de la ruta sea positiva
CREATE TRIGGER before_insert_ruta
BEFORE INSERT ON RUTA
FOR EACH ROW
BEGIN
    IF NEW.longitud <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La longitud debe ser mayor que 0';
    END IF;
END //

-- Impide modificar el creador de una ruta una vez creada
CREATE TRIGGER before_update_ruta_creador
BEFORE UPDATE ON RUTA
FOR EACH ROW
BEGIN
    IF NEW.id_creador != OLD.id_creador THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede cambiar el creador de una ruta';
    END IF;
END //

-- Mensaje descriptivo al intentar crear una lista con nombre duplicado
CREATE TRIGGER before_insert_lista_duplicada
BEFORE INSERT ON LISTA
FOR EACH ROW
BEGIN
    DECLARE v_count INT;
    SELECT COUNT(*) INTO v_count FROM LISTA WHERE nombre_lista = NEW.nombre_lista AND id_usuario = NEW.id_usuario;
    IF v_count > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ya tienes una lista con ese nombre';
    END IF;
END //

DELIMITER ;

