package com.usuarios.beta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ══════════════════════════════════════════════════════════
 *  PUNTO DE ENTRADA — Microservicio de USUARIOS (ms-usuarios)
 * ══════════════════════════════════════════════════════════
 *
 * ARQUITECTURA DE MICROSERVICIOS:
 * Este es un proyecto Maven INDEPENDIENTE con su propio proceso,
 * su propio puerto (8081) y su propia base de datos (mi_base_datos).
 * No comparte nada en runtime con los otros módulos: si cae, el resto
 * del sistema sigue vivo. Esa autonomía es la esencia de un
 * microservicio (y la evidencia física: otro puerto + otra BD).
 *
 * CÓMO ARRANCA:
 *  1. main() invoca SpringApplication.run(...).
 *  2. Spring crea el CONTEXTO (contenedor IoC) y registra como beans
 *     las clases anotadas que encuentra escaneando com.usuarios.beta
 *     (por defecto escanea el paquete de esta clase hacia abajo:
 *     models no lleva anotaciones; controler.UsuarioController sí).
 *  3. Levanta Tomcat embebido en el puerto de application.properties.
 *  4. DispatcherServlet enruta cada petición al controller.
 */
@SpringBootApplication
public class BetaApplication {

	/**
	 * Arranca contenedor + servidor embebido.
	 *
	 * @param args argumentos de consola (no se usan).
	 */
	public static void main(String[] args) {
		SpringApplication.run(BetaApplication.class, args);
	}

}
