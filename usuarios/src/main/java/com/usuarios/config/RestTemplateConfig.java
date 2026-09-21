package com.usuarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración de RestTemplate para comunicación inter-microservicios.
 *
 * RestTemplate es el cliente HTTP síncrono de Spring: permite que este
 * microservicio consuma APIs REST de otros (vehiculos, productos).
 *
 * Patrón aplicado: BEAN FACTORY METHOD — @Bean en un @Configuration.
 * Spring llama a este método UNA vez y almacena el resultado como
 * singleton en el contenedor IoC. Cada clase que pida un RestTemplate
 * recibe la misma instancia (eficiente en memoria).
 *
 * Uso típico:
 *   @Autowired
 *   private RestTemplate restTemplate;
 *   Vehiculo v = restTemplate.getForObject("http://localhost:8080/api/vehiculos/1", Vehiculo.class);
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
