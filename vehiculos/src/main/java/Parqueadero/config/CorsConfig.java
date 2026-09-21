package Parqueadero.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración CORS (Cross-Origin Resource Sharing) para ms-parqueadero.
 *
 * CORS es un mecanismo de seguridad del navegador que controla qué
 * dominios externos pueden hacer peticiones a esta API. Sin esta
 * configuración, el frontend en otro puerto/dominio recibiría errores
 * de bloqueo CORS.
 *
 * Flujo del preflight:
 *  1. El navegador envía un OPTIONS antes del POST/PUT/DELETE real.
 *  2. Este configurer responde con los headers permitidos.
 *  3. Si el preflight pasa, el navegador envía la petición real.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}
