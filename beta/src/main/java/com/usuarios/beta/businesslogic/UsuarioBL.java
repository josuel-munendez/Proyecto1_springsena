package com.usuarios.beta.businesslogic;

import com.usuarios.beta.models.Usuario;
import com.usuarios.beta.persistence.UsuarioPersistency;

import java.util.List;

/**
 * ══════════════════════════════════════════════════════════
 *  CAPA DE NEGOCIO (Business Logic / BLL) — Microservicio de Usuarios
 * ══════════════════════════════════════════════════════════
 *
 * Es el "cerebro" del microservicio: aquí viven las REGLAS DEL NEGOCIO
 * (qué datos son válidos, qué condiciones deben cumplirse) y es la ÚNICA
 * vía entre el Controller y la base de datos.
 *
 * SEPARACIÓN DE RESPONSABILIDADES (arquitectura por capas):
 *  - Controller     → traduce HTTP ↔ Java. NO valida reglas.
 *  - BL (esta capa) → valida reglas y orquesta la operación.
 *  - Persistency    → ejecuta SQL. NO decide nada de negocio.
 *
 * PATRÓN DE DISEÑO — INYECCIÓN DE DEPENDENCIAS MANUAL (constructor):
 * La capa de negocio NO fabrica su dependencia a escondidas: la RECIBE
 * por constructor (o crea una por defecto). Beneficios:
 *  - PRUEBAS: se puede inyectar un UsuarioPersistency falso (mock).
 *  - BAJO ACOPLAMIENTO: BL depende del concepto "persistencia", no de
 *    cómo construirla.
 * En Spring esto lo hace el contenedor con @Autowired; hacerlo a mano
 * primero enseña el principio que hay debajo (IoC: la inversión del
 * control — el flujo de las dependencias lo controla quien USA la
 * clase, no la clase misma).
 *
 * BUENA PRÁCTICA — DEFENSE IN DEPTH:
 * El frontend ya valida en JavaScript, PERO aquí se valida OTRA vez.
 * ¿Por qué duplicar? Porque el frontend se puede saltar (Postman,
 * curl, un atacante): el backend nunca debe confiar ciegamente en
 * lo que le llega.
 */
public class UsuarioBL {

    /**
     * Dependencia de acceso a datos. `final` → obliga a inicializarla
     * en TODOS los constructores y evita reasignaciones accidentales.
     */
    private final UsuarioPersistency persistence;

    /** Constructor por defecto: usa su propia persistencia real. */
    public UsuarioBL() {
        this.persistence = new UsuarioPersistency();
    }

    /**
     * Constructor con inyección manual de dependencias.
     *
     * @param persistence persistencia a utilizar (real o de prueba).
     */
    public UsuarioBL(UsuarioPersistency persistence) {
        this.persistence = persistence;
    }

    /**
     * VALIDA un usuario contra las reglas del negocio.
     *
     * Técnica: GUARD CLAUSES + FAIL FAST — cada regla se evalúa en
     * secuencia plana y retorna false inmediatamente al fallar,
     * evitando ifs anidados difíciles de leer.
     *
     * Reglas implementadas:
     *  1. Objeto no nulo.
     *  2. Nombre obligatorio.
     *  3. Correo obligatorio.
     *  4. Saldo no negativo.
     *
     * @param u usuario a validar.
     * @return true solo si pasa TODAS las reglas.
     */
    public boolean validarUsuario(Usuario u) {
        if (u == null) {
            System.out.println("Error: el usuario es nulo.");
            return false;
        }

        String nombre = u.getNombre();
        if (nombre == null || nombre.isBlank()) {
            System.out.println("Error: el nombre es obligatorio.");
            return false;
        }

        String correo = u.getCorreo();
        if (correo == null || correo.isBlank()) {
            System.out.println("Error: el correo es obligatorio.");
            return false;
        }

        if (u.getSaldo() < 0) {
            System.out.println("Error: el saldo no puede ser negativo.");
            return false;
        }

        // Si llegamos aquí, el usuario es válido.
        return true;
    }

    /**
     * CREATE lógico — Registra un usuario: valida y SOLO SI ES VÁLIDO
     * delega en la persistencia (el negocio decide, los datos obedecen).
     *
     * @param u usuario a guardar.
     * @return true si pasó la validación y la BD lo insertó.
     */
    public boolean registrarUsuario(Usuario u) {
        if (validarUsuario(u)) {
            return persistence.guardarUsuario(u);
        }
        return false;
    }

    /**
     * DELETE lógico — Elimina un usuario validando antes el id
     * (no nulo y positivo: evita borrar filas por accidente).
     *
     * @param id identificador del usuario.
     * @return true si se eliminó correctamente.
     */
    public boolean eliminarUsuario(Long id) {
        if (id == null || id <= 0) {
            System.out.println("Error: id inválido.");
            return false;
        }
        return persistence.eliminarUsuario(id);
    }

    /**
     * UPDATE lógico — Actualiza un usuario existente. Exige que el
     * objeto traiga su id (sin id no hay a quién actualizar) y luego
     * aplica las mismas validaciones del alta.
     *
     * @param u usuario con id y datos nuevos.
     * @return true si se actualizó correctamente.
     */
    public boolean actualizarUsuario(Usuario u) {
        if (u == null || u.getId() == null) {
            System.out.println("Error: el usuario y su id son obligatorios.");
            return false;
        }
        if (validarUsuario(u)) {
            return persistence.actualizarUsuario(u);
        }
        return false;
    }

    /**
     * READ masivo — Lista todos los usuarios.
     * Delega directo (listar no tiene reglas especiales hoy).
     *
     * @return lista de usuarios (vacía si no hay, nunca null).
     */
    public List<Usuario> listarUsuarios() {
        return persistence.listarUsuarios();
    }

    /**
     * READ individual — Busca un usuario por id tras validar el id.
     *
     * @param id identificador buscado.
     * @return el usuario o null si el id es inválido o no existe.
     */
    public Usuario obtenerUsuario(Long id) {
        if (id == null || id <= 0) {
            System.out.println("Error: id inválido.");
            return null;
        }
        return persistence.obtenerUsuario(id);
    }
}
