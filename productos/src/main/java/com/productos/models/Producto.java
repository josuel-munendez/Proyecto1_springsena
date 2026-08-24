package com.productos.models;

/**
 * ══════════════════════════════════════════════════════════
 *  CAPA DE MODELO (Entidad) — Microservicio de Productos
 * ══════════════════════════════════════════════════════════
 *
 * POJO que mapea la tabla `producto` de db_productos: cada atributo
 * corresponde a una columna. Viaja entre capas y entre frontend y
 * backend convertido a JSON por Jackson.
 *
 * PATRÓN JAVA BEAN (convención):
 *  - Atributos `private` + getters/setters públicos estándar.
 *  - Constructor vacío OBLIGATORIO para que Jackson pueda crear el
 *    objeto antes de llenarlo con el JSON del frontend.
 *  - Los nombres del JSON salen de los getters: getPrecioBase() →
 *    "precioBase" en el frontend.
 *
 * BUENAS PRÁCTICAS APLICADAS:
 *  - ENCAPSULAMIENTO: atributos privados; el estado solo cambia por
 *    sus setters (principio OOP visto en clase).
 *  - Getters de booleanos primitivos con prefijo `is` (isActivo,
 *    isAprobado): estándar JavaBeans; Jackson los serializa como
 *    "activo" y "aprobado".
 *  - Nombres camelCase SIN tildes/ñ ni guiones bajos: el borrador
 *    original usaba precio_base / fecha_actualización (identificador
 *    con tilde es válido en Java pero va contra la convención).
 *  - Las FECHAS como String: este módulo no calcula nada con ellas,
 *    solo las MUESTRA. MySQL las genera y actualiza solo, así Java
 *    no necesita lógica de fechas (simplicidad deliberada).
 */
public class Producto {

    /** Identificador único autogenerado por AUTO_INCREMENT en MySQL. */
    private Long id;

    /** Nombre comercial del producto. Obligatorio (regla de negocio). */
    private String nombre;

    /** Descripción corta para el catálogo. */
    private String descripcion;

    /** Precio base en pesos, sin impuestos. Nunca negativo (regla). */
    private double precioBase;

    /**
     * Disponible para la venta.
     * MySQL lo guarda como BOOLEAN (TINYINT(1)): true=1, false=0.
     */
    private boolean activo;

    /** Indica si pasó el control de calidad interno. */
    private boolean aprobado;

    /** Fecha/hora de creación — la pone MySQL con DEFAULT CURRENT_TIMESTAMP. Solo lectura. */
    private String fechaCreacion;

    /** Última modificación — la refresca MySQL con ON UPDATE CURRENT_TIMESTAMP. Solo lectura. */
    private String fechaActualizacion;

    /**
     * Constructor vacío. Requerido por Jackson para deserializar
     * el JSON que envía el frontend (@RequestBody).
     */
    public Producto() {
    }

    /**
     * Constructor completo: crea un producto ya inicializado.
     *
     * @param id                 identificador autogenerado por la BD.
     * @param nombre             nombre comercial.
     * @param descripcion        descripción corta.
     * @param precioBase         precio base sin impuestos (>= 0).
     * @param activo             disponible para venta.
     * @param aprobado           pasó control de calidad.
     * @param fechaCreacion      fecha de creación (la genera la BD).
     * @param fechaActualizacion última modificación (la genera la BD).
     */
    public Producto(Long id, String nombre, String descripcion, double precioBase,
                    boolean activo, boolean aprobado,
                    String fechaCreacion, String fechaActualizacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioBase = precioBase;
        this.activo = activo;
        this.aprobado = aprobado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    /** @return identificador único del producto. */
    public Long getId() {
        return id;
    }

    /** @param id establece el identificador (normalmente lo pone la BD). */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return nombre comercial del producto. */
    public String getNombre() {
        return nombre;
    }

    /** @param nombre establece el nombre (obligatorio según negocio). */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** @return descripción corta del producto. */
    public String getDescripcion() {
        return descripcion;
    }

    /** @param descripcion establece la descripción. */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /** @return precio base sin impuestos. */
    public double getPrecioBase() {
        return precioBase;
    }

    /** @param precioBase establece el precio (debe ser >= 0 según negocio). */
    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    /** @return true si está disponible para la venta. */
    public boolean isActivo() {
        return activo;
    }

    /** @param activo marca/desmarca la disponibilidad de venta. */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /** @return true si pasó control de calidad. */
    public boolean isAprobado() {
        return aprobado;
    }

    /** @param aprobado marca/desmarca la aprobación de calidad. */
    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

    /** @return fecha de creación tal como la devuelve MySQL (String). */
    public String getFechaCreacion() {
        return fechaCreacion;
    }

    /** @param fechaCreacion asigna la fecha de creación (uso interno/mapeo). */
    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /** @return fecha de última actualización (String desde MySQL). */
    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    /** @param fechaActualizacion asigna la fecha de actualización (mapeo). */
    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
