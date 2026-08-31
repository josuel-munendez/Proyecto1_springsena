package springsena.vehiculos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * ══════════════════════════════════════════════════════════
 *  PUNTO DE ENTRADA — Microservicio de VEHÍCULOS (ms-parqueadero)
 * ══════════════════════════════════════════════════════════
 *
 * @ComponentScan: escanea controllers y business logic.
 * @EntityScan: detecta entidades JPA en Parqueadero.entity.
 * @EnableJpaRepositories: detecta repositorios JPA en Parqueadero.repository.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"springsena.vehiculos", "Parqueadero"})
@EntityScan(basePackages = "Parqueadero.entity")
@EnableJpaRepositories(basePackages = "Parqueadero.repository")
public class VehiculosApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehiculosApplication.class, args);
	}

}
