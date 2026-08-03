package Parqueadero.entity;

public class Vehiculo {
    private long id;
    private String placa;
    private String marca;
    private String modelo;
    private String propetario;

    public Vehiculo() {
    }

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
