package Parqueadero.controler;

import Parqueadero.businesslogic.BLVehiculo;
import Parqueadero.entity.Vehiculo;
import org.springframework.web.bind.annotation.*;

/**
 * ══════════════════════════════════════════════════════════
 *  CAPA DE CONTROL (Controller) — Módulo Vehículos
 * ══════════════════════════════════════════════════════════
 *
 * Puerta de entrada HTTP del microservicio. Traduce peticiones web
 * a llamadas de la capa de negocio y sus respuestas a JSON.
 *
 * ANOTACIONES Y CÓMO FUNCIONAN:
 *  - @RestController: marca la clase como manejadora de peticiones y
 *    hace que TODO método devuelva su objeto serializado a JSON en el
 *    cuerpo de la respuesta (equivale a @Controller + @ResponseBody).
 *    Jackson es quien convierte el objeto Vehiculo → JSON usando los
 *    getters del JavaBean.
 *  - @RequestMapping("/ControllerVehiculo"): prefijo común de las
 *    rutas de esta clase (todas empiezan igual).
 *  - @GetMapping / @DeleteMapping: mapean verbos HTTP específicos
 *    (leer / eliminar) respetando la semántica REST.
 *  - @RequestParam("placa"): captura un parámetro de la URL
 *    (?placa=ABC123), distinto de @PathVariable que toma un trozo
 *    de la ruta (/vehiculos/ABC123).
 *
 * PATRÓN DE DISEÑO — FRONT CONTROLLER (implícito):
 * Aunque aquí solo vemos esta clase, TODAS las peticiones HTTP llegan
 * primero al DispatcherServlet de Spring (un único "recepcionista"),
 * que decide qué método de qué controller atiende cada ruta.
 * Este controller es un "handler" de ese Front Controller.
 *
 * PATRÓN DE DISEÑO — SINGLETON POR BEAN DE SPRING:
 * Spring crea UNA sola instancia de esta clase (scope singleton por
 * defecto) y la reutiliza para todas las peticiones. Por eso debe ser
 * SIN ESTADO (stateless): no guarda variables de una petición a otra,
 * solo la dependencia `final` e inmutable hacia el BL. Así es segura
 * ante múltiples usuarios simultáneos.
 */
@RestController("")
@RequestMapping("/ControllerVehiculo")
public class ControllerVehiculo {

    /**
     * DELETE /ControllerVehiculo/eliminar?placa=ABC123
     * Elimina un vehículo por placa.
     *
     * Flujo: recibe el parámetro → crea su capa de negocio → delega.
     * El Controller NO valida reglas complejas ni toca datos: SOLO
     * traduce HTTP ↔ llamadas Java (separación de responsabilidades).
     *
     * @param placa placa recibida por query string.
     */
    @DeleteMapping("/eliminar")
    public void deleteVehiculo(@RequestParam String placa){
        BLVehiculo bl = new BLVehiculo();
        bl.eliminarVehiculo(placa);
    }

    /**
     * GET /ControllerVehiculo/consultar?placa=ABC123
     * Consulta un vehículo por placa y lo devuelve como JSON.
     *
     * Incluye una validación rápida de formato en el borde (longitud
     * de placa) como PRIMERA LÍNEA DE DEFENSA; la validación completa
     * vive en el BL. Si falla, devuelve null (JSON vacío).
     *
     * @param placa placa recibida por query string.
     * @return el vehículo encontrado o null.
     */
    @GetMapping("/consultar")
    public Vehiculo consultarVehiculo(@RequestParam String placa){
        if (placa.length() == 6){
            BLVehiculo vr = new BLVehiculo();
            return vr.consultarVehiculo(placa);
        }
        return null;
    }
}
