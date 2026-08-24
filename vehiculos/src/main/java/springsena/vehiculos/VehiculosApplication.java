package springsena.vehiculos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * ══════════════════════════════════════════════════════════
 *  PUNTO DE ENTRADA — Microservicio de VEHÍCULOS (ms-parqueadero)
 * ══════════════════════════════════════════════════════════
 *
 * CÓMO ARRANCA UNA APP SPRING BOOT:
 *  1. main() llama a SpringApplication.run(...).
 *  2. Spring crea el CONTEXTO DE APLICACIÓN (el "contenedor IoC"):
 *     una caja donde vive TODOS los objetos que Spring administra
 *     (los "beans": controllers, services, repositorios...).
 *  3. Levanta además un servidor web embebido (Tomcat) en el puerto
 *     definido en application.properties (8080).
 *  4. A partir de ahí, cada petición HTTP entra por DispatcherServlet
 *     (Front Controller) y se enruta al método del controller que
 *     coincida con la ruta y el verbo.
 *
 * ANOTACIONES:
 *  - @SpringBootApplication = 3 anotaciones en una:
 *      · @Configuration  → esta clase puede definir beans.
 *      · @EnableAutoConfiguration → Spring configura solo lo típico
 *        (Tomcat, Jackson, datasource...) según lo que encuentre.
 *      · @ComponentScan  → busca clases anotadas (@RestController,
 *        @Service, @Repository...) para registrarlas como beans.
 *  - @ComponentScan(basePackages = {...}): le decimos explícitamente
 *    QUÉ paquetes escanear. Escaneamos el paquete base y "Parqueadero"
 *    porque sus clases viven fuera de la jerarquía estándar.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"springsena.vehiculos", "Parqueadero"})
public class VehiculosApplication {

	/**
	 * Arranca el contenedor de Spring y el servidor embebido.
	 *
	 * @param args argumentos de línea de comandos (no se usan).
	 */
	public static void main(String[] args) {
		SpringApplication.run(VehiculosApplication.class, args);
	}

}
