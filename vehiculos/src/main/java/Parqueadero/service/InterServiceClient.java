package Parqueadero.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Servicio de comunicación inter-microservicios.
 *
 * Consume APIs REST de otros microservicios del sistema:
 *  - ms-usuarios (usuarios, puerto 8081)
 *  - ms-productos (productos, puerto 8082)
 *
 * PATRÓN DE DISEÑO — SERVICE LOCATOR + REST CLIENT:
 * Centraliza las llamadas HTTP en un solo lugar reutilizable.
 * Si cambia la URL de un microservicio, se cambia aquí únicamente.
 *
 * MANEJO DE ERRORES:
 * Si el microservicio remoto no está disponible, se registra el error
 * con SLF4J y se retorna null (fail-open graceful degradation).
 */
@Service
public class InterServiceClient {

    private static final Logger log = LoggerFactory.getLogger(InterServiceClient.class);

    private final RestTemplate restTemplate;

    private static final String URL_USUARIOS = "http://localhost:8081/api/usuarios";
    private static final String URL_PRODUCTOS = "http://localhost:8082/api/productos";

    @Autowired
    public InterServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Obtiene todos los usuarios del microservicio de usuarios.
     *
     * @return lista de usuarios o lista vacía si el servicio no responde.
     */
    public List<?> listarUsuarios() {
        try {
            @SuppressWarnings("unchecked")
            List<?> usuarios = restTemplate.getForObject(URL_USUARIOS, List.class);
            return usuarios != null ? usuarios : List.of();
        } catch (Exception e) {
            log.error("Error al conectar con ms-usuarios: {}", e.getMessage());
            return List.of();
        }
    }

    /**
     * Obtiene todos los productos del microservicio de productos.
     *
     * @return lista de productos o lista vacía si el servicio no responde.
     */
    public List<?> listarProductos() {
        try {
            @SuppressWarnings("unchecked")
            List<?> productos = restTemplate.getForObject(URL_PRODUCTOS, List.class);
            return productos != null ? productos : List.of();
        } catch (Exception e) {
            log.error("Error al conectar con ms-productos: {}", e.getMessage());
            return List.of();
        }
    }
}
