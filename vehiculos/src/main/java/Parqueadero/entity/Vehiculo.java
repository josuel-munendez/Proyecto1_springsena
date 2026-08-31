package Parqueadero.entity;

import jakarta.persistence.*;

/**
 * ══════════════════════════════════════════════════════════
 *  CAPA DE MODELO (Entidad JPA) — Módulo Vehículos / Parqueadero
 * ══════════════════════════════════════════════════════════
 *
 * Representa UN vehículo del parqueadero. Ahora es una entidad JPA
 * que se mapea directamente a una tabla en la base de datos.
 *
 * ANOTACIONES JPA:
 *  - @Entity: indica que esta clase se persiste en la BD.
 *  - @Table(name="vehiculos"): nombre de la tabla en la BD.
 *  - @Id + @GeneratedValue: marca la clave primaria autogenerada.
 *  - @Column: define propiedades de cada columna.
 */
@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 6, unique = true)
    private String placa;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false, length = 4)
    private String modelo;

    @Column(nullable = false)
    private String propetario;

    /** Constructor vacío: requerido por JPA y Jackson. */
    public Vehiculo() {
    }

    /**
     * Constructor completo.
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
