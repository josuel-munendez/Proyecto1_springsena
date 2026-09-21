package com.productos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración de RestTemplate para comunicación inter-microservicios.
 *
 * RestTemplate es el cliente HTTP síncrono de Spring: permite que este
 * microservicio consuma APIs REST de otros (vehiculos, usuarios).
 *
 * Patrón aplicado: BEAN FACTORY METHOD — @Bean en un @Configuration.
 * Spring llama a este método UNA vez y almacena el resultado como
 * singleton en el contenedor IoC. Cada clase que pida un RestTemplate
 * recibe la misma instancia (eficiente en memoria).
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
