package Parqueadero.repository;

import Parqueadero.entity.Vehiculo;

public class VehiculoRepository {
    //conectar a mysql y ejecutar el delete con placa
    public boolean eliminarVehiculo(String placa){
        return true;
    }

    public Vehiculo consultarVehiculo(String placa){
        Vehiculo v = new Vehiculo();
        v.setPlaca(placa);
        v.setMarca("Mazda");
        return v;
    }
}
