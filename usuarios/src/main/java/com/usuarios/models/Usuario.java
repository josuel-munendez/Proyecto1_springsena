package com.usuarios.models;

/**
 * ══════════════════════════════════════════════════════════
 *  CAPA DE MODELO (Entidad) — Microservicio de Usuarios
 * ══════════════════════════════════════════════════════════
 *
 * POJO que mapea 1 a 1 la tabla `usuario`: cada atributo corresponde
 * a una columna. Es el objeto con el que viajan los datos entre capas
 * (el Controller lo recibe convertido desde JSON por Jackson y la
 * persistencia lo llena leyendo un ResultSet).
 *
 * PATRÓN JAVA BEAN (convención, no librería):
 *  - Atributos `private` + getters/setters públicos.
 *  - Constructor vacío OBLIGATORIO: Jackson crea primero el objeto
 *    vacío y después llama a los setters uno por uno mientras lee el
 *    JSON (deserialización por reflexión). Sin ese constructor, la
 *    petición POST del frontend fallaría con 500.
 *  - Getters/setters estándar → el JSON usa esos nombres
 *    (getSaldo() → "saldo" en JSON).
 *
 * BUENAS PRÁCTICAS APLICADAS:
 *  - ENCAPSULAMIENTO (OOP): nadie modifica `saldo` directamente; solo
 *    vía setSaldo(). Centraliza el acceso al estado.
 *  - Tipos correctos: `Long` para id (puede ser null cuando aún no se
 *    ha guardado) vs primitivos para datos siempre presentes.
 */
public class Usuario {

    /** Identificador único autogenerado por AUTO_INCREMENT en MySQL. */
    private Long id;

    /** Nombre completo del usuario. Obligatorio (regla de negocio). */
    private String nombre;

    /** Dirección de residencia. */
    private String direccion;

    /** Teléfono de contacto. */
    private int telefono;

    /** Correo electrónico. Obligatorio (regla de negocio). */
    private String correo;

    /** Saldo de la cuenta en pesos. Nunca negativo (regla de negocio). */
    private int saldo;

    /**
     * Constructor vacío. Lo exige Jackson para deserializar el JSON
     * que envía el frontend (@RequestBody).
     */
    public Usuario() {
    }

    /**
     * Constructor completo: crea un usuario ya inicializado.
     *
     * @param id        identificador autogenerado por la BD.
     * @param nombre    nombre completo.
     * @param direccion dirección de residencia.
     * @param telefono  teléfono de contacto.
     * @param correo    correo electrónico.
     * @param saldo     saldo inicial (no negativo).
     */
    public Usuario(Long id, String nombre, String direccion, int telefono, String correo, int saldo) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.saldo = saldo;
    }

    /** @return el identificador único del usuario. */
    public Long getId() {
        return id;
    }

    /** @param id nuevo identificador (lo asigna normalmente la BD). */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return nombre completo del usuario. */
    public String getNombre() {
        return nombre;
    }

    /** @param nombre establece el nombre completo. */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** @return dirección de residencia. */
    public String getDireccion() {
        return direccion;
    }

    /** @param direccion establece la dirección. */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /** @return teléfono de contacto. */
    public int getTelefono() {
        return telefono;
    }

    /** @param telefono establece el teléfono. */
    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    /** @return correo electrónico. */
    public String getCorreo() {
        return correo;
    }

    /** @param correo establece el correo electrónico. */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /** @return saldo actual de la cuenta. */
    public int getSaldo() {
        return saldo;
    }

    /** @param saldo establece el saldo (debe ser >= 0 según negocio). */
    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }
}
