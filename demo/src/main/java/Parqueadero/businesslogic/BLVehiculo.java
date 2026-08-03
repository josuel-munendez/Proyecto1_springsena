package Parqueadero.businesslogic;

import Parqueadero.entity.Vehiculo;

public class BLVehiculo {
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
                } else {                    // ← este else corresponde al if de placa,
                    // pero está dentro del if de marca (falta cerrar antes)
                    System.out.println("Placa incompleta");
                }
            } else {
                System.out.println("Datos vacios");
            }
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
}