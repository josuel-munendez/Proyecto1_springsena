package com.productos.businesslogic;

import com.productos.models.Producto;
import com.productos.persistence.ProductoPersistence;

import java.util.List;

/**
 * ══════════════════════════════════════════════════════════
 *  CAPA DE NEGOCIO (Business Logic) — Microservicio de Productos
 * ══════════════════════════════════════════════════════════
 *
 * "Cerebro" del microservicio: aplica las REGLAS DEL NEGOCIO antes de
 * tocar la base de datos y actúa como ÚNICO intermediario entre el
 * Controller y la persistencia.
 *
 * SEPARACIÓN DE RESPONSABILIDADES:
 *  - Controller     → traduce HTTP ↔ Java (no valida reglas).
 *  - BL (esta capa) → valida reglas y decide si se opera o no.
 *  - Persistence    → ejecuta SQL (no decide nada).
 *
 * PATRÓN DE DISEÑO — INYECCIÓN DE DEPENDENCIAS MANUAL (constructor):
 * La persistencia se RECIBE por constructor en vez de fabricarse
 * dentro de cada método. Beneficios: pruebas con mocks posibles y
 * bajo acoplamiento. Es el fundamento del @Autowired de Spring (IoC:
 * quien usa la clase controla sus dependencias).
 *
 * PATRÓN LIGERO — FACHADA (Facade):
 * Para el Controller, esta clase es una interfaz sencilla
 * (guardar/eliminar/actualizar/listar) que esconde detrás el flujo
 * validar → persistir. El controller no sabe que existen validaciones
 * ni SQL: solo llama al BL.
 *
 * BUENA PRÁCTICA — DEFENSE IN DEPTH:
 * El frontend valida en JavaScript, pero aquí se valida OTRA vez,
 * porque el frontend se puede saltar (Postman, curl). El backend jamás
 * confía ciegamente en el cliente.
 */
public class BLProducto {

    /**
     * Dependencia de acceso a datos. `final` → se inicializa en TODOS
     * los constructores y no puede reasignarse.
     */
    private final ProductoPersistence persistence;

    /** Constructor por defecto: usa su propia persistencia real. */
    public BLProducto() {
        this.persistence = new ProductoPersistence();
    }

    /**
     * Constructor con inyección manual de dependencias.
     *
     * @param persistence persistencia a utilizar (real o mock de prueba).
     */
    public BLProducto(ProductoPersistence persistence) {
        this.persistence = persistence;
    }

    /**
     * VALIDA un producto contra las reglas del negocio.
     *
     * Técnica: GUARD CLAUSES + FAIL FAST — validaciones planas en
     * secuencia; al fallar la primera, retorna false sin seguir.
     *
     * Reglas implementadas:
     *  1. Objeto no nulo.
     *  2. Nombre obligatorio (no nulo ni vacío/isBlank).
     *  3. Precio base no negativo.
     *
     * Nota: descripcion, activo y aprobado NO tienen reglas estrictas
     * hoy (opcionales); agregarlas sería solo añadir otra guard clause.
     *
     * @param p producto a validar.
     * @return true solo si pasa TODAS las reglas.
     */
    public boolean validarProducto(Producto p) {
        // Regla 1: el objeto no puede ser nulo.
        if (p == null) {
            System.out.println("Error: el producto es nulo.");
            return false;
        }

        // Regla 2: el nombre no puede ser nulo ni estar vacío.
        String nombre = p.getNombre();
        if (nombre == null || nombre.isBlank()) {
            System.out.println("Error: el nombre es obligatorio.");
            return false;
        }

        // Regla 3: el precio no puede ser negativo.
        if (p.getPrecioBase() < 0) {
            System.out.println("Error: el precio no puede ser negativo.");
            return false;
        }

        return true;
    }

    /**
     * CREATE lógico — Guarda un producto: valida primero y SOLO SI ES
     * VÁLIDO delega en la persistencia.
     *
     * @param p producto a guardar.
     * @return true si pasó validación + inserción en BD.
     */
    public boolean guardarProducto(Producto p) {
        if (validarProducto(p)) {
            return persistence.guardarProducto(p);
        }
        return false;
    }

    /**
     * DELETE lógico — Elimina por id tras validarlo (no nulo, > 0),
     * evitando borrados accidentales con ids basura.
     *
     * @param id identificador del producto.
     * @return true si se eliminó correctamente.
     */
    public boolean eliminarProducto(Long id) {
        if (id == null || id <= 0) {
            System.out.println("Error: id inválido.");
            return false;
        }
        return persistence.eliminarProducto(id);
    }

    /**
     * UPDATE lógico — Actualiza un producto. Exige id presente
     * (sin id no hay a quién actualizar) y re-aplica las validaciones
     * del alta sobre los nuevos valores.
     *
     * @param p producto con id y datos nuevos.
     * @return true si se actualizó correctamente.
     */
    public boolean actualizarProducto(Producto p) {
        if (p == null || p.getId() == null) {
            System.out.println("Error: el producto y su id son obligatorios.");
            return false;
        }
        if (validarProducto(p)) {
            return persistence.actualizarProducto(p);
        }
        return false;
    }

    /**
     * READ masivo — Lista todo el catálogo. Delega directo porque
     * listar no tiene reglas especiales hoy.
     *
     * @return lista de productos (vacía si no hay, nunca null).
     */
    public List<Producto> listarProductos() {
        return persistence.listarProductos();
    }

    /**
     * READ individual — Busca un producto por id tras validar el id.
     *
     * @param id identificador buscado.
     * @return el producto o null si el id es inválido o no existe.
     */
    public Producto obtenerProducto(Long id) {
        if (id == null || id <= 0) {
            System.out.println("Error: id inválido.");
            return null;
        }
        return persistence.obtenerProducto(id);
    }
}
