package Parqueadero.entity;

/**
 * ══════════════════════════════════════════════════════════
 *  CAPA DE MODELO (Entidad) — Módulo Vehículos / Parqueadero
 * ══════════════════════════════════════════════════════════
 *
 * Representa UN vehículo del parqueadero: es el objeto con el que
 * el resto de las capas trabaja en memoria (nunca viaja SQL hacia
 * el Controller ni HTML hacia el negocio).
 *
 * QUÉ ES UN POJO: "Plain Old Java Object". Una clase Java normal,
 * SIN anotaciones de frameworks. Ventaja: se puede mover entre
 * proyectos o probar sin depender de Spring ni de la BD.
 *
 * PATRÓN JAVA BEAN: convención de la industria donde una entidad se
 * compone de atributos privados + constructor vacío + getters/setters
 * públicos. ¿Por qué importa? Porque herramientas como Jackson
 * (la librería que convierte JSON ↔ objetos en los controllers)
 * funcionan POR REFLEXIÓN leyendo esas convenciones: si un getter
 * no sigue el estándar, el JSON no se genera bien.
 *
 * BUENAS PRÁCTICAS APLICADAS:
 *  - ENCAPSULAMIENTO: atributos `private`; nadie fuera de la clase
 *    modifica el estado directamente, solo a través de setters.
 *    Esto protege invariantes (ej. que la placa no se corrompa).
 *  - Dos constructores: vacío (lo exige Jackson para crear el objeto
 *    antes de llenarlo) y completo (comodidad para pruebas).
 */
public class Vehiculo {

    /** Identificador único autogenerado por la base de datos. */
    private long id;

    /** Placa del vehículo: 6 caracteres, es su identificador natural. */
    private String placa;

    /** Marca comercial (Renault, Mazda, etc.). Obligatoria. */
    private String marca;

    /** Modelo/año representado con 4 caracteres (ej. "2024"). */
    private String modelo;

    /**
     * Nombre del dueño del vehículo.
     * Nota: el nombre tiene un typo histórico ("propetario") que se
     * conserva para no romper el resto del código visto en clase;
     * lo correcto sería "propietario".
     */
    private String propetario;

    /** Constructor vacío: requerido por frameworks (Jackson, ORMs). */
    public Vehiculo() {
    }

    /**
     * Constructor completo: crea un vehículo ya inicializado.
     *
     * @param id         identificador autogenerado.
     * @param propetario nombre del dueño.
     * @param modelo     modelo/año (4 caracteres).
     * @param marca      marca comercial.
     * @param placa      placa (6 caracteres).
     */
    public Vehiculo(long id, String propetario, String modelo, String marca, String placa) {
        this.id = id;
        this.propetario = propetario;
        this.modelo = modelo;
        this.marca = marca;
        this.placa = placa;
    }

    public void setId(long idM) {
        id = idM;
    }

    public long getId() {
        return id;
    }

    public void setPlaca(String placaM) {
        placa = placaM;
    }

    public String getPlaca() {
        return placa;
    }

    public void setMarca(String marcaM) {
        marca = marcaM;
    }

    public String getMarca() {
        return marca;
    }

    public void setModelo(String modeloM) {
        modelo = modeloM;
    }

    public String getModelo() {
        return modelo;
    }

    public void setPropetario(String propetarioM) {
        propetario = propetarioM;
    }

    public String getPropetario() {
        return propetario;
    }
}
