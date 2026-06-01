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
    FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario) ON DELETE CASCADE
);

-- 4. Tabla VALORACION 
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

