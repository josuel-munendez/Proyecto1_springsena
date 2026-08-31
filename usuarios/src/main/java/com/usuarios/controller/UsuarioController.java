package com.usuarios.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usuarios.businesslogic.UsuarioBL;
import com.usuarios.models.Usuario;

/**
 * ══════════════════════════════════════════════════════════
 *  CAPA DE CONTROL (Controller / API REST) — Microservicio de Usuarios
 * ══════════════════════════════════════════════════════════
 *
 * Puerta de entrada HTTP: expone el CRUD completo en /api/usuarios.
 * El frontend (static/index.html) lo consume con fetch desde JS.
 *
 * MAPA CRUD ↔ HTTP ↔ MÉTODO:
 *   Create  → POST   → guardar()
 *   Read    → GET    → listar() y obtener(id)
 *   Update  → PUT    → actualizar()
 *   Delete  → DELETE → eliminar(id)
 *
 * ANOTACIONES Y CÓMO FUNCIONAN:
 *  - @RestController = @Controller + @ResponseBody. Spring registra
 *    esta clase como bean (SINGLETON por defecto) y cada método
 *    devuelve su objeto convertido a JSON automáticamente (por
 *    Jackson, usando los getters del JavaBean Usuario).
 *  - @RequestMapping("/api/usuarios"): prefijo común de todas las
 *    rutas de la clase.
 *  - @GetMapping/@PostMapping/@PutMapping/@DeleteMapping: asocian
 *    cada método a un verbo HTTP respetando la semántica REST.
 *  - @RequestBody: al llegar un POST/PUT, Jackson lee el JSON del
 *    cuerpo y construye el objeto Usuario (usa el constructor vacío
 *    + setters).
 *  - @PathVariable Long id: captura {id} de la ruta; Spring además
 *    CONVIERTE el texto "5" al tipo declarado (Long). Si alguien
 *    manda "/abc", responde 400 sin que escribamos código.
 *
 * PATRÓN DE DISEÑO — FRONT CONTROLLER:
 * Las peticiones no llegan "directamente" aquí: primero pasan por el
 * DispatcherServlet de Spring MVC (único punto de entrada), que usa
 * estas anotaciones como mapa para elegir el método correcto.
 *
 * BUENA PRÁCTICA — CONTROLLER DELGADO (thin controller):
 * Este método NO valida reglas ni escribe SQL. Solo recibe, delega
 * en el BL y devuelve. Así la lógica vive en UN solo lugar y se puede
 * reutilizar desde otros clientes (Postman, otro microservicio...).
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    /**
     * Única dependencia, `final` e inyectada una vez en el constructor.
     * Como el bean es singleton, esta instancia vive durante toda la
     * app; por eso la clase no guarda estado de peticiones (thread-safe).
     */
    private final UsuarioBL bl;

    /** Inyección manual por constructor (fundamento previo a @Autowired). */
    public UsuarioController() {
        this.bl = new UsuarioBL();
    }

    /**
     * GET /api/usuarios
     * Devuelve todos los usuarios. La lista se serializa a JSON
     * automáticamente: [{"id":1,"nombre":"Ana"...}, ...].
     *
     * @return lista completa de usuarios (vacía si no hay datos).
     */
    @GetMapping
    public List<Usuario> listar() {
        return bl.listarUsuarios();
    }

    /**
     * GET /api/usuarios/{id}
     * Devuelve UN usuario (lo usa el botón "Editar" del frontend).
     *
     * @param id identificador tomado de la ruta.
     * @return el usuario o null si no existe (JSON vacío).
     */
    @GetMapping("/{id}")
    public Usuario obtener(@PathVariable Long id) {
        return bl.obtenerUsuario(id);
    }

    /**
     * POST /api/usuarios
     * Crea un usuario nuevo a partir del JSON del cuerpo.
     *
     * @param u usuario deserializado del JSON (@RequestBody).
     * @return true si el negocio lo validó y la BD lo insertó;
     *         false si falló alguna regla (el frontend muestra error).
     */
    @PostMapping
    public boolean guardar(@RequestBody Usuario u) {
        return bl.registrarUsuario(u);
    }

    /**
     * PUT /api/usuarios
     * Actualiza un usuario existente (el JSON debe traer su id).
     *
     * @param u usuario con id y valores nuevos.
     * @return true si se actualizó; false si faltó id o falló validación.
     */
    @PutMapping
    public boolean actualizar(@RequestBody Usuario u) {
        return bl.actualizarUsuario(u);
    }

    /**
     * DELETE /api/usuarios/{id}
     * Elimina el usuario cuyo id viaja en la ruta.
     *
     * @param id identificador del usuario.
     * @return true si existía y fue eliminado.
     */
    @DeleteMapping("/{id}")
    public boolean eliminar(@PathVariable Long id) {
        return bl.eliminarUsuario(id);
    }
}
