-- ============================================================
--  Script de la base de datos del microservicio de USUARIOS.
--  Ejecutar en MySQL (Workbench o consola) antes de correr la app.
-- ============================================================

CREATE DATABASE IF NOT EXISTS mi_base_datos;
USE mi_base_datos;

-- Tabla que usará el CRUD (coincide con la clase com.usuarios.beta.models.Usuario).
CREATE TABLE IF NOT EXISTS usuario (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    direccion  VARCHAR(200),
    telefono   INT,
    correo     VARCHAR(100),
    saldo      INT
);

-- Datos de prueba opcionales.
INSERT INTO usuario (nombre, direccion, telefono, correo, saldo) VALUES
('Ana García',    'Calle 1 #2-3', 3105551234, 'ana@mail.com',   50000),
('Luis Pérez',    'Cra 4 #5-6',  3205559876, 'luis@mail.com',  120000),
('María López',   'Av 7 #8-9',   3005554321, 'maria@mail.com', 75000);
