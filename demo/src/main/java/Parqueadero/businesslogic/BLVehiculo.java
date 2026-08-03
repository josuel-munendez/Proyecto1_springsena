package Parqueadero.businesslogic;

import Parqueadero.entity.Vehiculo;

import Parqueadero.repository.VehiculoRepository;

public class BLVehiculo {
    private final VehiculoRepository vr;

    public BLVehiculo() {
    }
    public BLVehiculo(VehiculoRepository vr) {
        this.vr = vr;
    }

    public boolean validarVehiculo(long id, String placa, String modelo) {
        return false;
    }

    public boolean validarVehiculo(Vehiculo v) {
        if (v != null) {
            if (v.getPlaca().length() == 6) {
                if (!v.getMarca().isBlank()) {
                    if (v.getModelo().length() == 4) {
                        return true;
                    } else {
                        System.out.println("Modelo vacio");
                    }
                } else {
                    System.out.println("Sin Marca");
                }
            } else {
                System.out.println("Placa incompleta");
            }
        } else {
            System.out.println("Datos vacios");
        }

        /* Correccion con Codigo Limpio: */
            if (v == null) {
                System.out.println("Datos vacios");
                return false;
            }

            String placa = v.getPlaca();
            if (placa == null || placa.length() != 6) {
                System.out.println("Placa incompleta (debe tener 6 caracteres)");
                return false;
            }

            String marca = v.getMarca();
            if (marca == null || marca.isBlank()) {
                System.out.println("Marca vacía");
                return false;
            }

            String modelo = v.getModelo();
            if (modelo == null || modelo.length() != 4) {
                System.out.println("Modelo inválido (debe tener 4 caracteres)");
                return false;
            }

            /*return true;*/
        return false;
    }

    public boolean eliminarVehiculo(String placa){
        if (placa.length() == 6){
            return vr.eliminarVehiculo(placa);
        }
        return false;
    }


    public Vehiculo consultarVehiculo(String placa){
        if (placa.length() == 6){
            return vr.consultarVehiculo(placa);
        }else {
            return null;
        }
    }
}