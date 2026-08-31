package com.productos.controler;

import com.productos.businesslogic.BLProducto;
import com.productos.models.Producto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ══════════════════════════════════════════════════════════
 *  CAPA DE CONTROL (Controller / API REST) — Microservicio de Productos
 * ══════════════════════════════════════════════════════════
 *
 * Puerta de entrada HTTP del microservicio: expone el CRUD en
 * /api/productos. El frontend (static/index.html) lo consume con
 * fetch desde JavaScript.
 *
 * MAPA CRUD ↔ HTTP ↔ MÉTODO (semántica REST):
 *   Create  → POST   → guardar()
 *   Read    → GET    → listar() y obtener(id)
 *   Update  → PUT    → actualizar()
 *   Delete  → DELETE → eliminar(id)
 *
 * ANOTACIONES Y CÓMO FUNCIONAN:
 *  - @RestController = @Controller + @ResponseBody. Registra la clase
 *    como bean SINGLETON de Spring y serializa cada retorno a JSON
 *    con Jackson (usa los getters del JavaBean Producto).
 *  - @RequestMapping("/api/productos"): prefijo común de las rutas.
 *  - @GetMapping/@PostMapping/@PutMapping/@DeleteMapping: asocian
 *    cada método a su verbo HTTP.
 *  - @RequestBody: convierte el JSON del POST/PUT en un objeto
 *    Producto (constructor vacío + setters, por reflexión).
 *  - @PathVariable Long id: captura {id} de la ruta y lo convierte a
 *    Long automáticamente; si no es numérico responde 400 solo.
 *
 * EQUIVALENCIA CON MVC/MVT DE DJANGO:
 *  - Esta clase hace el papel de la "vista" (views.py) de Django.
 *  - La "V" aquí es doble: el JSON de respuesta Y static/index.html.
 *
 * PATRÓN DE DISEÑO — FRONT CONTROLLER:
 * Toda petición entra primero por DispatcherServlet (único
 * recepcionista de Spring MVC), que usa estas anotaciones como mapa
 * para elegir el método. Esta clase es uno de sus handlers.
 *
 * BUENA PRÁCTICA — CONTROLLER DELGADO (thin controller):
 * Sin SQL, sin reglas de negocio: recibe → delega en BL → devuelve.
 * La lógica vive en UN lugar reutilizable desde cualquier cliente.
 */
@RestController
@RequestMapping("/api/productos")
public class ControllerProducto {

    /**
     * Única dependencia, `final` e inyectada una sola vez. Como el bean
     * es singleton y SIN ESTADO, es segura ante peticiones concurrentes.
     */
    private final BLProducto bl;

    /** Inyección manual por constructor (fundamento previo a @Autowired). */
    public ControllerProducto() {
        this.bl = new BLProducto();
    }

    /**
     * GET /api/productos
     * Lista todo el catálogo serializado como array JSON.
     *
     * @return lista completa de productos (vacía si no hay datos).
     */
    @GetMapping
    public List<Producto> listar() {
        return bl.listarProductos();
    }

    /**
     * GET /api/productos/{id}
     * Devuelve UN producto (lo usa el botón "Editar" del frontend).
     *
     * @param id identificador tomado de la ruta.
     * @return el producto o null si no existe (JSON vacío).
     */
    @GetMapping("/{id}")
    public Producto obtener(@PathVariable Long id) {
        return bl.obtenerProducto(id);
    }

    /**
     * POST /api/productos
     * Crea un producto nuevo a partir del JSON del cuerpo.
     *
     * @param p producto deserializado del JSON (@RequestBody).
     * @return true si el negocio validó y la BD insertó;
     *         false si falló alguna regla (frontend muestra error).
     */
    @PostMapping
    public boolean guardar(@RequestBody Producto p) {
        return bl.guardarProducto(p);
    }

    /**
     * PUT /api/productos
     * Actualiza un producto existente (el JSON debe traer su id).
     *
     * @param p producto con id y valores nuevos.
     * @return true si se actualizó; false si faltó id o falló validación.
     */
    @PutMapping
    public boolean actualizar(@RequestBody Producto p) {
        return bl.actualizarProducto(p);
    }

    /**
     * DELETE /api/productos/{id}
     * Elimina el producto cuyo id viaja en la ruta.
     *
     * @param id identificador del producto.
     * @return true si existía y fue eliminado.
     */
    @DeleteMapping("/{id}")
    public boolean eliminar(@PathVariable Long id) {
        return bl.eliminarProducto(id);
    }
}
