-- ============================================================
--  Script de la base de datos del microservicio de PRODUCTOS.
--  Ejecutar en MySQL (Workbench o consola) antes de correr la app.
-- ============================================================

CREATE DATABASE IF NOT EXISTS db_productos;
USE db_productos;

-- Tabla que usará el CRUD (coincide con la clase com.productos.models.Producto).
-- Las fechas las genera MySQL automáticamente:
--   fecha_creacion      -> DEFAULT CURRENT_TIMESTAMP
--   fecha_actualizacion -> ON UPDATE CURRENT_TIMESTAMP
CREATE TABLE IF NOT EXISTS producto (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre              VARCHAR(100) NOT NULL,
    descripcion         VARCHAR(200),
    precio_base         DOUBLE NOT NULL DEFAULT 0,
    activo              BOOLEAN NOT NULL DEFAULT TRUE,
    aprobado            BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_creacion      DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Datos de prueba del catálogo.
INSERT INTO producto (nombre, descripcion, precio_base, activo, aprobado) VALUES
('Teclado mecánico',    'Teclado RGB switches rojos', 250000, TRUE, TRUE),
('Mouse inalámbrico',   'Mouse óptico 2.4 GHz',        80000, TRUE, FALSE),
('Monitor 24 pulgadas', 'Monitor Full HD IPS',        650000, TRUE, TRUE);
