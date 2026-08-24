package com.productos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Microservicio de PRODUCTOS (ms-productos).
 *
 * Proyecto independiente con arquitectura por capas:
 *   controler -> businesslogic -> persistence -> MySQL
 *
 * Puerto: 8082  |  Base de datos: db_productos
 * Frontend: src/main/resources/static/index.html (consume la API con fetch).
 */
@SpringBootApplication
public class ProductosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductosApplication.class, args);
	}

}
