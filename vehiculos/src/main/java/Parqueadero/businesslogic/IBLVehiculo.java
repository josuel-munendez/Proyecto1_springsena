package Parqueadero.businesslogic;

import Parqueadero.entity.Vehiculo;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de negocio para el módulo de Vehículos.
 * Define el contrato que cualquier implementación debe cumplir.
 */
public interface IBLVehiculo {

    List<Vehiculo> listarTodos();

    Optional<Vehiculo> buscarPorId(long id);

    Optional<Vehiculo> buscarPorPlaca(String placa);

    Vehiculo guardar(Vehiculo v);

    boolean eliminar(long id);

    boolean validarVehiculo(Vehiculo v);

    List<Vehiculo> listarPaginado(int pagina, int tamanio);
}
