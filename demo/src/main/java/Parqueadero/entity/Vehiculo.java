package Parqueadero.entity;

public class Vehiculo {
    protected long id;
    protected String placa;
    protected String marca;
    protected String modelo;
    protected String propetario;

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
